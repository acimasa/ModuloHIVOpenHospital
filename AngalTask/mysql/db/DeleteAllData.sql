#version 28-5-2006
#version 12-6-2011

#tabelle senza relazioni
delete from PREGNANTTREATMENTTYPE;
delete from VERSION;
delete from LOG;
delete from HELP;
delete from DELIVERYRESULTTYPE;
delete from DELIVERYTYPE;
delete from HOSPITAL;
delete from AGETYPE;
delete from MEDICALSRSTOCKMOV_N;
delete from PRICELISTS;
delete from PRICES;
delete from PRICESOTHERS;
delete from BILLITEMS;
delete from BILLPAYMENTS;
delete from BILLS;
delete from THERAPIES;
delete from VISITS;

#tabelle con relazioni, ordine inverso di cancellazione
delete from groupmenu;
delete from user;
delete from menuitem;
delete from usergroup;
delete from opd;
delete from laboratoryrow;
delete from laboratory;
delete from examrow;
delete from exam;
delete from examtype;
delete from medicaldsrlot;
delete from medicaldsrstockmov;
delete from medicaldsrstockmovtype;
delete from medicaldsr;
delete from medicaldsrtype;
delete from vaccine;
delete from patientvaccine;
delete from malnutritioncontrol;
delete from admission;
delete from admissiontype;
delete from dischargetype;
delete from operation;
delete from operationtype;
delete from disease;
delete from diseasetype;
delete from patient;
delete from ward;

