package com.kiosk.printer;

import android.app.Activity;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;


public class PrinterManager implements ReceiveListener
{
    public interface PrinterCallback
    {
        void onSuccess();

        void onError(String strError);

        void onWarning(String strWarning);
    }

    private PrinterCallback pCallback = null;
    private PrinterDiscovery printerDiscovery = null;
    private Printer mPrinter = null;
    private Activity mContext = null;

    public PrinterManager(Activity context, PrinterCallback pCallback)
    {
        this.mContext = context;
        this.pCallback = pCallback;

    }


    public void print(final String data)
    {
        // if 1st time, get discovery usb id
        int times = PrinterHelper.readTime(mContext);
        if (times == 1)
        {
            printerDiscovery = new PrinterDiscovery(mContext);
            printerDiscovery.startDiscovery(new PrinterDiscovery.dCallback()
            {
                @Override
                public void onSuccess(PrinterModel printerModel)
                {
                    PrinterHelper.writeUSBID(mContext, printerModel.printerTarget);
                    PrinterHelper.writeTime(mContext, 2);
                    runPrintReceiptSequence(data, pCallback);
                }

                @Override
                public void onError(String strError)
                {
                    PrinterHelper.writeTime(mContext, 1);
                    pCallback.onError(PrinterError.ERROR_CONNECT_TO_PRINTER_BUT_RETRYING);
                }
            });
        }
        else // if from second time, use exist usb id, but if cannot connect then call discovery again
        {
            runPrintReceiptSequence(data, pCallback);
        }
    }


    private boolean runPrintReceiptSequence(String data, PrinterCallback pCallback)
    {
        if (!initializeObject(pCallback))
        {
            return false;
        }

        if (!createReceiptData(data, pCallback))
        {
            finalizeObject();
            return false;
        }

        if (!printData(pCallback))
        {
            finalizeObject();
            return false;
        }

        return true;
    }

    private boolean createReceiptData(String data, PrinterCallback pCallback)
    {
        String method = "";

        StringBuilder textData = new StringBuilder();


        if (mPrinter == null)
        {
            pCallback.onError(PrinterError.ERROR_PRINTER_NULL);
            return false;
        }

        try
        {
            method = "addTextAlign";
            mPrinter.addTextAlign(Printer.ALIGN_CENTER);

            method = "addFeedLine";
            textData.append(data + "\n");

            method = "addText";
            mPrinter.addText(textData.toString());
            textData.delete(0, textData.length());


            mPrinter.addFeedLine(1);

            method = "addCut";
            mPrinter.addCut(Printer.CUT_FEED);
        }
        catch (Exception e)
        {
            pCallback.onError(PrinterError.ERROR_CREATE_PRINT_DATA);
            //ShowMsg.showException(e, method, mContext);
            return false;
        }

        textData = null;

        return true;

    }


    private boolean printData(PrinterCallback pCallback)
    {
        if (mPrinter == null)
        {
            pCallback.onError(PrinterError.ERROR_PRINTER_NULL);
            return false;
        }

        if (!connectPrinter(pCallback))
        {
            return false;
        }

        PrinterStatusInfo status = mPrinter.getStatus();

        checkPrinterStatus(status, pCallback);

        if (!isPrintable(status, pCallback))
        {


            try
            {
                mPrinter.disconnect();
            }
            catch (Exception ex)
            {
                // Do nothing
                pCallback.onError(PrinterError.ERROR_DISCONNECT_FROM_PRINTER);
            }
            return false;
        }

        try
        {
            mPrinter.sendData(Printer.PARAM_DEFAULT);
        }
        catch (Exception e)
        {

            pCallback.onError(PrinterError.ERROR_SEND_DATA_TO_PRINT);

            //ShowMsg.showException(e, "sendData", mContext);
            try
            {
                mPrinter.disconnect();
            }
            catch (Exception ex)
            {
                // Do nothing
                pCallback.onError(PrinterError.ERROR_DISCONNECT_FROM_PRINTER);
            }
            return false;
        }

        return true;
    }

    private boolean initializeObject(PrinterCallback pCallback)
    {
        try
        {
            mPrinter = new Printer(Printer.TM_T82, Printer.MODEL_ANK, mContext);
        }
        catch (Exception e)
        {
            pCallback.onError(PrinterError.ERROR_INITIALIZE_OBJECT);
            //ShowMsg.showException(e, "Printer", mContext);
            return false;
        }

        mPrinter.setReceiveEventListener(this);

        return true;
    }

    private void finalizeObject()
    {
        if (mPrinter == null)
        {
            return;
        }

        mPrinter.clearCommandBuffer();

        mPrinter.setReceiveEventListener(null);

        mPrinter = null;
    }

    private boolean connectPrinter(PrinterCallback pCallback)
    {

        boolean isBeginTransaction = false;

        if (mPrinter == null)
        {
            pCallback.onError(PrinterError.ERROR_PRINTER_NULL);
            return false;
        }

        try
        {
            mPrinter.connect(PrinterHelper.readUSBID(mContext), Printer.PARAM_DEFAULT);
        }
        catch (Exception e)
        {
            PrinterHelper.writeTime(mContext, 1);
            pCallback.onError(PrinterError.ERROR_CONNECT_TO_PRINTER_BUT_RETRYING);
            //ShowMsg.showException(e, "connect", mContext);
            return false;
        }

        try
        {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        }
        catch (Exception e)
        {
            //ShowMsg.showException(e, "beginTransaction", mContext);
            pCallback.onError(PrinterError.ERROR_BEGIN_TRANSACTION);
        }

        if (isBeginTransaction == false)
        {
            try
            {
                mPrinter.disconnect();
            }
            catch (Epos2Exception e)
            {
                // Do nothing
                pCallback.onError(PrinterError.ERROR_DISCONNECT_FROM_PRINTER);
                return false;
            }
        }

        return true;
    }

    private void disconnectPrinter()
    {
        if (mPrinter == null)
        {
            return;
        }

        try
        {
            mPrinter.endTransaction();
        }
        catch (final Exception e)
        {
        }

        try
        {
            mPrinter.disconnect();
        }
        catch (final Exception e)
        {
        }
        finalizeObject();
    }

    private boolean isPrintable(PrinterStatusInfo status, PrinterCallback pCallback)
    {
        if (status == null)
        {
            pCallback.onError(PrinterError.ERROR_PRINTER_IS_OFFLINE);
            return false;
        }

        if (status.getConnection() == Printer.FALSE)
        {
            pCallback.onError(PrinterError.ERROR_PRINTER_IS_OFFLINE);
            return false;
        }
        else if (status.getOnline() == Printer.FALSE)
        {
            pCallback.onError(PrinterError.ERROR_PRINTER_IS_OFFLINE);
            return false;
        }
        else
        {
            ;//print available
        }

        return true;
    }


    private void checkPrinterStatus(PrinterStatusInfo status, PrinterCallback pCallback)
    {
        if (status == null)
        {
            pCallback.onError(PrinterError.ERROR_PRINTER_STATUS_IS_NULL);
            return;
        }

        if (status.getOnline() == Printer.FALSE)
        {

            pCallback.onError(PrinterError.ERROR_PRINTER_IS_OFFLINE);
            //mContext.getString(R.string.handlingmsg_err_offline);
        }
        if (status.getConnection() == Printer.FALSE)
        {
            pCallback.onError(PrinterError.ERROR_PRINTER_IS_OFFLINE);
            //mContext.getString(R.string.handlingmsg_err_no_response);
        }
        if (status.getCoverOpen() == Printer.TRUE)
        {
            pCallback.onError(PrinterError.ERROR_ROLL_COVER_IS_OPENING);
            //mContext.getString(R.string.handlingmsg_err_cover_open);
        }
        if (status.getPaper() == Printer.PAPER_EMPTY)
        {
            pCallback.onError(PrinterError.ERROR_PAPER_IS_EMPTY);
            //mContext.getString(R.string.handlingmsg_err_receipt_end);
        }
        if (status.getPaperFeed() == Printer.TRUE || status.getPanelSwitch() == Printer.SWITCH_ON)
        {
            pCallback.onError(PrinterError.ERROR_FEED_IS_OPENING);
            //mContext.getString(R.string.handlingmsg_err_paper_feed);
        }
        if (status.getErrorStatus() == Printer.MECHANICAL_ERR || status.getErrorStatus() == Printer.AUTOCUTTER_ERR)
        {
            pCallback.onError(PrinterError.ERROR_CUTTER_JAMMED_PAPER_PLEASE_RESTART_PRINTER);
            //mContext.getString(R.string.handlingmsg_err_autocutter);
            //mContext.getString(R.string.handlingmsg_err_need_recover);
        }
        if (status.getErrorStatus() == Printer.UNRECOVER_ERR)
        {
            pCallback.onError(PrinterError.ERROR_FATAL_PLEASE_RESTART_PRINTER);
            //mContext.getString(R.string.handlingmsg_err_unrecover);
        }
        if (status.getErrorStatus() == Printer.AUTORECOVER_ERR)
        {
            if (status.getAutoRecoverError() == Printer.HEAD_OVERHEAT)
            {
                pCallback.onError(PrinterError.ERROR_OVER_HEAD);
                //mContext.getString(R.string.handlingmsg_err_overheat);
                //mContext.getString(R.string.handlingmsg_err_head);
            }
            if (status.getAutoRecoverError() == Printer.MOTOR_OVERHEAT)
            {
                pCallback.onError(PrinterError.ERROR_OVER_HEAD);
                //mContext.getString(R.string.handlingmsg_err_overheat);
                //mContext.getString(R.string.handlingmsg_err_motor);
            }
            if (status.getAutoRecoverError() == Printer.BATTERY_OVERHEAT)
            {
                pCallback.onError(PrinterError.ERROR_OVER_HEAD);
                //mContext.getString(R.string.handlingmsg_err_overheat);
                //mContext.getString(R.string.handlingmsg_err_battery);
            }
            if (status.getAutoRecoverError() == Printer.WRONG_PAPER)
            {
                pCallback.onError(PrinterError.ERROR_WRONG_PAPER);
                // mContext.getString(R.string.handlingmsg_err_wrong_paper);
            }
        }
        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_0)
        {
            pCallback.onWarning(PrinterError.ERROR_BATTERY_IS_USED_UP);
            //mContext.getString(R.string.handlingmsg_err_battery_real_end);
        }
        if (status.getPaper() == Printer.PAPER_NEAR_END)
        {
            pCallback.onWarning(PrinterError.WARNING_PAPER_NEAR_END);
            // handlingmsg_warn_receipt_near_end


        }

        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1)
        {
            pCallback.onWarning(PrinterError.WARNING_BATTERY_NEAR_END);
            // handlingmsg_warn_battery_near_end

        }
    }


    @Override
    public void onPtrReceive(final Printer printer, final int code, final PrinterStatusInfo status, final String printJobId)
    {
        //checkPrinterStatus(status, pCallback);

        pCallback.onSuccess();

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                disconnectPrinter();
            }
        }).start();

    }

}

