package com.kiosk.printer;

import android.content.Context;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.discovery.DeviceInfo;
import com.epson.epos2.discovery.Discovery;
import com.epson.epos2.discovery.DiscoveryListener;
import com.epson.epos2.discovery.FilterOption;

import java.util.HashMap;

/**
 * Created by HuynhBinh on 12/3/15.
 */
public class PrinterDiscovery
{


    private Context context = null;
    private FilterOption mFilterOption = null;
    private DiscoveryListener mDiscoveryListener = null;

    private static final String PRINTER_NAME = "TM Printer";


    public interface dCallback
    {
        void onSuccess(PrinterModel printerModel);

        void onError(String strError);
    }


    public PrinterDiscovery(Context context)
    {
        this.context = context;
        initFilterOption();
        //initDiscoveryListener();
    }

    public void initFilterOption()
    {
        mFilterOption = new FilterOption();

        mFilterOption.setPortType(Discovery.PORTTYPE_USB);
        mFilterOption.setDeviceModel(Discovery.MODEL_ALL);
        mFilterOption.setDeviceType(Discovery.TYPE_PRINTER);
        mFilterOption.setEpsonFilter(Discovery.FILTER_NAME);

    }


    public void startDiscovery(final dCallback callback)
    {

        mDiscoveryListener = new DiscoveryListener()
        {
            @Override
            public void onDiscovery(final DeviceInfo deviceInfo)
            {
                HashMap<String, String> item = new HashMap<String, String>();
                item.put("PrinterName", deviceInfo.getDeviceName());
                item.put("Target", deviceInfo.getTarget());

                String printerName = deviceInfo.getDeviceName();
                String printerTarget = deviceInfo.getTarget();

                if (printerName != null)
                {
                    if (printerName.equalsIgnoreCase(PRINTER_NAME))
                    {
                        if (printerTarget != null)
                        {
                            PrinterModel printerModel = new PrinterModel();
                            printerModel.printerName = printerName;
                            printerModel.printerTarget = printerTarget;
                            callback.onSuccess(printerModel);
                        }
                    }
                }

            }
        };

        if (context == null)
        {
            callback.onError("Context is NULL");
        }

        if (mFilterOption == null)
        {
            callback.onError("FilterOption is NULL");
        }

        if (mDiscoveryListener == null)
        {
            callback.onError("DiscoveryListener is NULL");
        }

        try
        {
            Discovery.start(context, mFilterOption, mDiscoveryListener);

        }
        catch (Exception e)
        {
            callback.onError("Exception: " + e.getMessage());
            restartDiscovery();
        }
    }


    private void restartDiscovery()
    {
        while (true)
        {
            try
            {
                Discovery.stop();
                break;
            }
            catch (Epos2Exception e)
            {
                if (e.getErrorStatus() != Epos2Exception.ERR_PROCESSING)
                {

                    return;
                }
            }
        }


        try
        {
            Discovery.start(context, mFilterOption, mDiscoveryListener);
        }
        catch (Exception e)
        {
            return;
        }
    }


}
