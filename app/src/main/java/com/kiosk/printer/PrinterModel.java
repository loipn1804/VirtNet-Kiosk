package com.kiosk.printer;

/**
 * Created by HuynhBinh on 12/3/15.
 */
public class PrinterModel
{
    public String printerName;
    public String printerTarget;

    public PrinterModel()
    {
    }

    @Override
    public String toString()
    {
        return "PrinterModel{" +
                "printerName='" + printerName + '\'' +
                ", printerTarget='" + printerTarget + '\'' +
                '}';
    }
}
