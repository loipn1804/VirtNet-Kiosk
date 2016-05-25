package com.kiosk.model;

import java.io.File;

/**
 * Created by HuynhBinh on 12/8/15.
 */
public class Worker {
    public String worker_id;
    public String RFID;
    public String dormitory_id;
    public String kiosk_id;
    public String name;
    public String email;
    public String phone;
    public String password;
    public String FIN;
    public String DOB;
    public String gender;
    public String room;
    public String company;
    public String passport_no;
    public String nationality;
    public String DOE;
    public String permit_no;

    public Worker() {
        worker_id = "";
        RFID = "";
        dormitory_id = "";
        kiosk_id = "";
        name = "";
        email = "";
        phone = "";
        password = "";
        FIN = "";
        DOB = "";
        gender = "";
        room = "";
        company = "";
        passport_no = "";
        nationality = "";
        DOE = "";
        permit_no = "";
    }

    public File workerPhoto;

}
