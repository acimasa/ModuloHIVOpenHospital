-- version 29-8-2006

delete from HOSPITAL;
delete from MEDICALDSR;
delete from MEDICALDSRTYPE;
delete from MEDICALDSRSTOCKMOVTYPE;
delete from DELIVERYTYPE;
delete from DELIVERYRESULTTYPE;
delete from PREGNANTTREATMENTTYPE;
delete from EXAMROW;
delete from EXAM;
delete from EXAMTYPE;
delete from OPERATION;
delete from OPERATIONTYPE;
delete from DISEASE;
delete from DISEASETYPE;
delete from VACCINE;
delete from ADMISSIONTYPE;
delete from DISCHARGETYPE;


-- HOSPITAL
INSERT INTO HOSPITAL (HOS_ID_A,HOS_NAME,HOS_ADDR,HOS_CITY,HOS_TELE,HOS_FAX,HOS_EMAIL,HOS_LOCK) VALUES 
 ('STLUKE','St. Luke HOSPITAL - Angal','P.O. BOX 85 - NEBBI','ANGAL','+256 0472621076','+256 0','angal@ucmb.ug.co.',0);

-- MEDICALDSRTYPE
INSERT INTO MEDICALDSRTYPE (MDSRT_ID_A,MDSRT_DESC) VALUES 
 ('D','Drugs'),
 ('K','Chemical'),
 ('L','Laboratory'),
 ('S','Surgery');

-- MEDICALDSRSTOCKMOVTYPE
insert into MEDICALDSRSTOCKMOVTYPE (MMVT_ID_A,MMVT_DESC,MMVT_TYPE) values ('charge','Charge','+');
insert into MEDICALDSRSTOCKMOVTYPE (MMVT_ID_A,MMVT_DESC,MMVT_TYPE) values ('discharge','Discharge','-');

-- DELIVERYTYPE
insert into DELIVERYTYPE values ('N','NORMAL DELIVERY');
insert into DELIVERYTYPE values ('C','DELIVERY ASSISTED BY CESARIAN SECTION');
insert into DELIVERYTYPE values ('V','DELIVERY ASSISTED BY VACUUM EXTRACTION');

-- DELIVERYRESULTTYPE
INSERT INTO DELIVERYRESULTTYPE (DRT_ID_A,DRT_DESC) VALUES 
 ('A','LIVE BIRTH'),
 ('B','MACERATED STILLBIRTH'),
 ('M','MATERNAL DEATH'),
 ('N','NEWBORN DEATH'),
 ('S','FRESH STILL BIRTH');

-- PREGNANTTREATMENTTYPE
insert into PREGNANTTREATMENTTYPE values ('N','NEW ANC ATTENDANCE');
insert into PREGNANTTREATMENTTYPE values ('A','ANC RE-ATTENDANCE');
insert into PREGNANTTREATMENTTYPE values ('S1','FIRST DOSE WITH SP');
insert into PREGNANTTREATMENTTYPE values ('S2','SECOND DOSE WITH SP');
insert into PREGNANTTREATMENTTYPE values ('I1','IMMUNISATION 1');
insert into PREGNANTTREATMENTTYPE values ('I2','IMMUNISATION 2');
insert into PREGNANTTREATMENTTYPE values ('I3','IMMUNISATION 3');

-- EXAMTYPE
INSERT INTO EXAMTYPE (EXC_ID_A,EXC_DESC) VALUES 
 ('HB','1.Haematology'),
 ('BT','2.Blood transfusion'),
 ('PA','3.Parasitology'),
 ('BA','4.Bacteriology'),
 ('MC','5.Microscopy'),
 ('SE','6.Serology'),
 ('CH','7.Chemistry'),
 ('OC','8.Occult Blood'),
 ('OT','OTHER');

-- EXAM
INSERT INTO EXAM (EXA_ID_A,EXA_DESC,EXA_EXC_ID_A,EXA_PROC,EXA_DEFAULT,EXA_LOCK) VALUES 
 ('01.01','1.1 HB','HB',1,'>=12 (NORMAL)',1),
 ('01.02','1.2 WBC Count','HB',1,'4000 - 7000 (NORMAL)',1),
 ('01.03','1.3 Differential ','HB',1,'',0),
 ('01.04','1.4 Film Comment','HB',1,'',0),
 ('01.05','1.5 ESR','HB',1,'NORMAL',1),
 ('01.06','1.6 Sickling Test','HB',1,'NEGATIVE',1),
 ('02.01','2.1 Grouping','BT',1,'',2),
 ('02.02','2.2 Comb\'s Test','BT',1,'NEGATIVE',3),
 ('03.01','3.1 Blood Slide (Malaria)','PA',1,'NEGATIVE',1),
 ('03.02','3.2 Blood Slide (OTHERS, E.G. TRIUPHANOSOMIAS, MICRIFILARIA, LEISHMANIA, BORRELIA)','PA',1,'NEGATIVE',2),
 ('03.02.1','3.21 Trypanosomiasis','PA',1,'NEGATIVE',2),
 ('03.02.2','3.22 MICROFILARIA','PA',1,'NEGATIVE',1),
 ('03.02.3','3.23 LEISHMANIA','PA',1,'NEGATIVE',1),
 ('03.02.4','3.24 BORRELIA','PA',1,'NEGATIVE',1),
 ('03.03','3.3 STOOL MICROSCOPY','PA',2,'',2),
 ('03.04','3.4 URINE MICROSCOPY','PA',1,'NEGATIVE',1),
 ('03.05','3.5 TISSUE MICROSCOPY','PA',1,'NEGATIVE',1),
 ('03.06','3.6 CSF WET PREP','PA',1,'NEGATIVE',1),
 ('04.01','4.1 CULTURE AND SENSITIVITY (C&S) FOR HAEMOPHILUS INFUENZA TYPE B','BA',1,'NEGATIVE',2),
 ('04.02','4.2 C&S FOR SALMONELA TYPHI','BA',1,'NEGATIVE',1),
 ('04.03','4.3 C&S FOR VIBRO CHOLERA','BA',1,'NEGATIVE',1),
 ('04.04','4.4 C&S FOR SHIGELLA DYSENTERIAE','BA',1,'NEGATIVE',1),
 ('04.05','4.5 C&S FOR NEISSERIA MENINGITIDES','BA',1,'NEGATIVE',1),
 ('04.06','4.6 OTHER C&S ','BA',1,'NEGATIVE',1),
 ('05.01','5.1 WET PREP','MC',1,'NEGATIVE',1),
 ('05.02','5.2 GRAM STAIN','MC',1,'NEGATIVE',1),
 ('05.03','5.3 INDIA INK','MC',1,'NEGATIVE',1),
 ('05.04','5.4 LEISMANIA','MC',1,'NEGATIVE',1),
 ('05.05','5.5 ZN','MC',1,'NEGATIVE',1),
 ('05.06','5.6 WAYSON','MC',1,'NEGATIVE',1),
 ('05.07','5.7 PAP SMEAR','MC',1,'NEGATIVE',1),
 ('06.01','6.1 VDRL/RPR','SE',1,'NEGATIVE',1),
 ('06.02','6.2 TPHA','SE',1,'NEGATIVE',1),
 ('06.03','6.3 HIV','SE',1,'NEGATIVE',1),
 ('06.04','6.4 HEPATITIS','SE',1,'NEGATIVE',1),
 ('06.05','6.5 OTHERS E.G BRUCELLA, RHEUMATOID FACTOR, WEIL FELIX','SE',1,'NEGATIVE',1),
 ('06.06','6.6 PREGANCY TEST','SE',1,'NEGATIVE',1),
 ('07.01','7.1 PROTEIN','CH',1,'NEGATIVE',1),
 ('07.02','7.2 SUGAR','CH',1,'NORMAL',1),
 ('07.03','7.3 LFTS','CH',1,'NORMAL',1),
 ('07.03.1','7.3.1 BILIRUBIN TOTAL','CH',1,'< 1.2 (NORMAL)',3),
 ('07.03.2','7.3.2 BILIRUBIN DIRECT','CH',1,'< 1.2 (NORMAL)',7),
 ('07.03.3','7.3.3 GOT','CH',1,'<= 50 (NORMAL)',2),
 ('07.03.4','7.3.4 ALT/GPT','CH',1,'<= 50 (NORMAL)',1),
 ('07.04','7.4 RFTS','CH',1,'NORMAL',1),
 ('07.04.1','7.4.1 CREATININA','CH',1,'< 1.4 (NORMAL)',1),
 ('07.04.2','7.4.2 UREA','CH',1,'10-55 (NORMAL)',1),
 ('08.01','8.1 OCCULT BLOOD','OC',1,'NEGATIVE',1),
 ('09.01','9.1 URINALYSIS','OT',2,'',1);

-- EXAMROW
INSERT INTO EXAMROW (EXR_ID,EXR_EXA_ID_A,EXR_DESC) VALUES 
 (14,'05.02','NEGATIVE'),
 (15,'05.02','PNEUMOCOCCI'),
 (16,'05.02','MENINGOCOCCI'),
 (17,'05.02','HEMOPHILLUS INFL.'),
 (18,'05.02','CRYPTOCOCCI'),
 (19,'05.02','PLEAMORPHIC BACILLI'),
 (20,'05.02','STAPHYLOCOCCI'),
 (48,'07.03.1','<1.2 (NORMAL)'),
 (49,'07.03.1','1.2 - 5'),
 (50,'07.03.1','> 5'),
 (51,'07.04.1','< 1.4 (NORMAL)'),
 (52,'07.04.1','1.4 - 2.5'),
 (53,'07.04.1','> 2.5'),
 (56,'07.03.3','<= 50 (NORMAL)'),
 (57,'07.03.3','> 50'),
 (60,'01.02','4000 - 7000 (NORMAL)'),
 (61,'01.02','> 7000 (HIGH)'),
 (62,'01.02','< 4000 (LOW)'),
 (76,'09.01','SEDIMENTS'),
 (77,'09.01','SUGAR'),
 (78,'09.01','UROBILINOGEN'),
 (79,'09.01','BILIRUBIN'),
 (80,'09.01','PROTEIN'),
 (81,'09.01','HCG'),
 (83,'07.04.2','> 10'),
 (84,'07.04.2','10-55 (NORMAL)'),
 (85,'07.04.2','< 55'),
 (87,'01.01','6 - 12'),
 (88,'01.01','< 6'),
 (89,'01.01','>=12 (NORMAL)'),
 (90,'01.05','HIGH'),
 (91,'01.05','NORMAL'),
 (92,'01.05','LOW'),
 (93,'01.06','POSITIVE'),
 (94,'01.06','NEGATIVE');
INSERT INTO EXAMROW (EXR_ID,EXR_EXA_ID_A,EXR_DESC) VALUES 
 (96,'02.01','A RH+'),
 (97,'02.01','A RH-'),
 (98,'02.01','B RH+'),
 (99,'02.01','B RH-'),
 (100,'02.01','AB RH+'),
 (101,'02.01','AB RH-'),
 (102,'02.01','O RH+'),
 (103,'02.01','O RH-'),
 (104,'02.02','NEGATIVE'),
 (105,'02.02','POSITIVE'),
 (106,'03.01','NEGATIVE'),
 (107,'03.01','FEW'),
 (109,'03.01','+'),
 (110,'03.01','++'),
 (111,'03.01','+++'),
 (112,'03.01','++++'),
 (113,'03.02','POSITIVE'),
 (114,'03.02','NEGATIVE'),
 (116,'03.02.1','NEGATIVE'),
 (117,'03.02.2','POSITIVE'),
 (118,'03.02.2','NEGATIVE'),
 (119,'03.02.3','NEGATIVE'),
 (120,'03.02.3','POSITIVE'),
 (121,'03.02.4','NEGATIVE'),
 (122,'03.02.4','POSITIVE'),
 (125,'03.05','POSITIVE'),
 (126,'03.05','NEGATIVE'),
 (127,'03.06','POSITIVE'),
 (128,'03.06','NEGATIVE'),
 (129,'04.01','POSITIVE'),
 (130,'04.01','NEGATIVE'),
 (131,'04.02','POSITIVE'),
 (132,'04.02','NEGATIVE'),
 (133,'04.03','POSITIVE'),
 (134,'04.03','NEGATIVE'),
 (135,'04.04','POSITIVE'),
 (136,'04.04','NEGATIVE'),
 (137,'04.05','POSITIVE');
INSERT INTO EXAMROW (EXR_ID,EXR_EXA_ID_A,EXR_DESC) VALUES 
 (138,'04.05','NEGATIVE'),
 (139,'05.01','POSITIVE'),
 (140,'05.01','NEGATIVE'),
 (143,'05.03','POSITIVE'),
 (144,'05.03','NEGATIVE'),
 (145,'05.04','POSITIVE'),
 (146,'05.04','NEGATIVE'),
 (148,'05.05','NEGATIVE'),
 (149,'05.06','POSITIVE'),
 (150,'05.06','NEGATIVE'),
 (151,'05.07','POSITIVE '),
 (152,'05.07','NEGATIVE'),
 (153,'06.01','POSITIVE'),
 (154,'06.01','NEGATIVE'),
 (155,'06.02','POSITIVE'),
 (156,'06.02','NEGATIVE'),
 (157,'06.03','POSITIVE'),
 (158,'06.03','NEGATIVE'),
 (159,'06.04','POSITIVE'),
 (160,'06.04','NEGATIVE'),
 (161,'06.05','POSITIVE'),
 (162,'06.05','NEGATIVE'),
 (163,'06.06','POSITIVE'),
 (164,'06.06','NEGATIVE'),
 (165,'07.01','POSITIVE'),
 (166,'07.01','NEGATIVE'),
 (169,'07.03','HIGH'),
 (170,'07.03','LOW'),
 (171,'07.03','NORMAL'),
 (172,'07.04','HIGH'),
 (173,'07.04','LOW'),
 (174,'07.04','NORMAL'),
 (175,'08.01','POSITIVE'),
 (176,'08.01','NEGATIVE'),
 (185,'04.06','NORMAL'),
 (186,'05.05','+'),
 (187,'05.05','++');
INSERT INTO EXAMROW (EXR_ID,EXR_EXA_ID_A,EXR_DESC) VALUES 
 (188,'05.05','+++'),
 (189,'03.03','A..LUMBRICOIDES'),
 (190,'03.03','E.COLI CYSTS'),
 (191,'03.03','E.HISTOLYTICA'),
 (192,'03.03','E.VERMICULARIS'),
 (193,'03.03','G.LAMBLIA'),
 (194,'03.03','T.HOMINIS'),
 (195,'03.03','HOOK WORM'),
 (196,'03.03','S.MANSONI'),
 (197,'03.03','S.STERCORALIS'),
 (198,'03.03','TAENIA SAGINATA'),
 (199,'03.03','TAENIA SOLIUM'),
 (200,'03.03','TRICHURISI TRICHURA'),
 (201,'07.03.2','< 1.2 (NORMAL)'),
 (202,'07.03.2','1.2 - 5'),
 (203,'07.03.2','> 5'),
 (204,'07.03.4','<= 50 (NORMAL)'),
 (205,'07.03.4','> 50'),
 (206,'07.02','HIGH'),
 (207,'07.02','LOW'),
 (208,'07.02','NORMAL'),
 (209,'03.04','POSITIVE'),
 (210,'03.04','NEGATIVE'),
 (211,'03.02.1','POSITIVE'),
 (212,'03.03','HYMENOLEPIS NANA');

-- OPERATIONTYPE
insert into OPERATIONTYPE values ('AG','ABDOMINAL GENERAL SURGERY','MAJOR');
insert into OPERATIONTYPE values ('GY','GYNECOLOGICAL','MAJOR');
insert into OPERATIONTYPE values ('MG','MALE GENITOR-URINARY SYSTEM','MAJOR');
insert into OPERATIONTYPE values ('OB','OBSTETRICAL','MAJOR');
insert into OPERATIONTYPE values ('OR','ORTHOPEDICAL','MAJOR');
insert into OPERATIONTYPE values ('OS','OTHERS: SKIN AND SUBCUTANEOUS','MAJOR');

-- VACCINE
insert into VACCINE values ('1','BCG','C',0);
insert into VACCINE values ('10','TT VACCINE DOSE 1','P',0);
insert into VACCINE values ('11','TT VACCINE DOSE 2','P',0);
insert into VACCINE values ('12','TT VACCINE DOSE 3','P',0);
insert into VACCINE values ('13','TT VACCINE DOSE 4','P',0);
insert into VACCINE values ('14','TT VACCINE DOSE 5','P',0);
insert into VACCINE values ('15','TT VACCINE DOSE 2','N',0);
insert into VACCINE values ('16','TT VACCINE DOSE 3','N',0);
insert into VACCINE values ('17','TT VACCINE DOSE 4','N',0);
insert into VACCINE values ('18','TT VACCINE DOSE 5','N',0);
insert into VACCINE values ('2','POLIO 0 C','C',0);
insert into VACCINE values ('3','POLIO 1 C','C',0);
insert into VACCINE values ('4','POLIO 2 C','C',0);
insert into VACCINE values ('5','POLIO 3 C','C',0);
insert into VACCINE values ('6','DPT 1 - HepB + Hib 1','C',0);
insert into VACCINE values ('7','DPT 2 - HepB + Hib 1','C',0);
insert into VACCINE values ('8','DPT 3 - HepB + Hib 1','C',0);
insert into VACCINE values ('9','MEASLES','C',0);

-- ADMISSIONTYPE
INSERT INTO ADMISSIONTYPE (ADMT_ID_A,ADMT_DESC) VALUES 
 ('A','AMBULANCE'),
 ('I','SELF'),
 ('R','REFERRAL');

-- DISCHARGETYPE
INSERT INTO DISCHARGETYPE (DIST_ID_A,DIST_DESC) VALUES 
 ('B','REFERRED'),
 ('D','DEAD'),
 ('EQ','NORMAL DISCHARGE'),
 ('ES','ESCAPE');

-- DISEASETYPE
INSERT INTO DISEASETYPE (DCL_ID_A,DCL_DESC) VALUES 
 ('AO','5. All Other'),
 ('MP','3.MATERNAL AND PERINATAL DISEASES'),
 ('NC','4.NON-COMMUNICABLE DISEASES'),
 ('ND','1.NOTIFIABLE DISEASES'),
 ('OC','2.OTHER INFECTIOUS/COMMUNICABLE DISEASES');

-- DISEASE
INSERT INTO DISEASE (DIS_ID_A,DIS_DESC,DIS_DCL_ID_A,DIS_LOCK,DIS_OPD_INCLUDE,DIS_IPD_INCLUDE) VALUES 
 ('1','Acute Flaccid Paralysis','ND',0,1,1),
 ('10','Viral Haemorragic Fever','ND',0,1,1),
 ('100','Other malignant neoplasm','NC',0,1,1),
 ('101','Curable Ulcers','NC',0,1,1),
 ('102','Cerebro-vascular event','NC',0,1,1),
 ('103','Cardiac arrest','NC',0,1,1),
 ('104','Gastro-intestinal bleeding','NC',0,1,1),
 ('105','Respiratory distress','NC',0,1,1),
 ('106','Acute renal failure','NC',1,0,1),
 ('107','Acute sepsis','NC',1,0,1),
 ('108','Schostosomiasis','NC',0,1,1),
 ('11','Yellow Fever','ND',0,1,1),
 ('110','Pelvic Inflammatory DISEASEs (PID)','OC',0,1,1),
 ('111','Tetanus (over 28 days age)','OC',1,1,1),
 ('112','Obstructed Labour','MP',0,1,1),
 ('113','Other types of meningitis','OC',0,1,1),
 ('114','Schistosomiasis','OC',0,1,1),
 ('115','Sleeping sickness','OC',0,1,1),
 ('116','Malaria in pregnancy','MP',0,1,1),
 ('117','Injuries - (road traffic accident)','NC',0,1,1),
 ('12','Anaemia','NC',0,1,1);
INSERT INTO DISEASE (DIS_ID_A,DIS_DESC,DIS_DCL_ID_A,DIS_LOCK,DIS_OPD_INCLUDE,DIS_IPD_INCLUDE) VALUES 
 ('13','Dental DISEASE and conditions','NC',0,1,1),
 ('14','Diabetes Mellitus','NC',0,1,1),
 ('15','Gastro-intestinal DISEASEss (non infective)','NC',1,1,1),
 ('16','Hypertension','NC',0,1,1),
 ('17','Mental Illness','NC',0,1,1),
 ('18','Epilepsy','NC',0,1,1),
 ('19','Other cardio-vascular DISEASEs','NC',0,1,1),
 ('2','Cholera','ND',0,1,1),
 ('20','Severe malnutrition (marasmus,kwashiorkor,marasmic-kwash)','NC',6,1,0),
 ('22','Trauma-Domestic Violence','NC',0,1,1),
 ('23','Trauma-other intensional','NC',0,1,1),
 ('24','Trauma Road traffic accidents','NC',0,1,1),
 ('25','Trauma Other Non intensional','NC',0,1,1),
 ('26','Other complications of pregnancy','MP',1,0,1),
 ('27','Perinatal conditions','MP',0,1,1),
 ('28','Abortions','MP',0,1,1),
 ('29','AIDS','OC',0,1,1),
 ('3','Diarrhoea-Dysentry','ND',2,1,1),
 ('30','Diarrhoea-Not bloody','OC',0,1,1),
 ('31','Diarrhoea-Persistent','OC',0,1,1),
 ('32','Ear Infection','OC',1,0,1);
INSERT INTO DISEASE (DIS_ID_A,DIS_DESC,DIS_DCL_ID_A,DIS_LOCK,DIS_OPD_INCLUDE,DIS_IPD_INCLUDE) VALUES 
 ('33','Eye Infection','OC',0,1,1),
 ('34','Genital Inf.-Urethral discharge','OC',1,0,1),
 ('35','Genital Inf.-Vaginal discharge','OC',1,0,1),
 ('36','Genital Inf.-Ulcerative','OC',1,0,1),
 ('37','Intestinal Worms','OC',0,1,1),
 ('38','Leprosy','OC',0,1,1),
 ('39','Malaria','OC',0,1,1),
 ('4','Guinea Worm','ND',0,1,1),
 ('40','No pneumonia-cold or cough','OC',0,1,1),
 ('41','Onchocerciasis','OC',0,1,1),
 ('42','Pneumonia','OC',0,1,1),
 ('43','Skin DISEASEs','OC',0,1,1),
 ('44','Tuberculosis','OC',2,1,1),
 ('46','Typhoid Fever','OC',0,1,1),
 ('47','Urinary Tract Infections (UTI)','OC',2,1,1),
 ('48','Others(non specified)','OC',1,0,1),
 ('49','All Other DISEASEs (specify)','AO',2,1,1),
 ('5','Measles','ND',0,1,1),
 ('50','Other emerging infectious DISEASE (specify)','ND',0,1,1),
 ('56','Death in OPD','OC',0,1,1),
 ('57','ENT Conditions','OC',0,1,1),
 ('58','Eye Conditions','OC',0,1,1),
 ('59','Sexually transmited infections (STI)','OC',1,1,1);
INSERT INTO DISEASE (DIS_ID_A,DIS_DESC,DIS_DCL_ID_A,DIS_LOCK,DIS_OPD_INCLUDE,DIS_IPD_INCLUDE) VALUES 
 ('6','Meningitis','ND',0,1,1),
 ('60','Hepatitis','OC',1,0,1),
 ('61','Osteomyelitis','OC',1,0,1),
 ('62','Peritonitis','OC',1,0,1),
 ('63','Pyrexiaof unknown origin (PUO)','OC',1,0,1),
 ('64','Septicaemia','OC',1,0,1),
 ('65','High blood pressure in pregnancy','MP',0,1,1),
 ('66','Haemorrhage related to pregnancy (APH/PPH)','MP',0,1,1),
 ('67','Sepsis related to pregnancy','MP',1,0,1),
 ('68','Asthma','NC',0,1,1),
 ('69','Oral DISEASEs and condition','NC',0,1,1),
 ('7','Tetanus neonatal (less 28 days age)','ND',3,1,1),
 ('70','Endocrine and metabolic disorders (other)','NC',0,1,1),
 ('71','Anxiety disorders','NC',0,1,1),
 ('72','Mania','NC',0,1,1),
 ('73','Depression','NC',0,1,1),
 ('74','Schizophrenia','NC',0,1,1),
 ('75','Alcohol and drug abuse','NC',0,1,1),
 ('76','Childhood and mentle disorders','NC',0,1,1),
 ('77','Severe malnutrition (kwashiorkor)','NC',2,0,1),
 ('78','Severe malnutrition (marasmus)','NC',3,0,1),
 ('79','Severe malnutrition (marasmic-kwash)','NC',2,0,1);
INSERT INTO DISEASE (DIS_ID_A,DIS_DESC,DIS_DCL_ID_A,DIS_LOCK,DIS_OPD_INCLUDE,DIS_IPD_INCLUDE) VALUES 
 ('8','Plague','ND',0,1,1),
 ('80','Low weight for age','NC',0,1,1),
 ('81','Injuries - (trauma due to other causes)','NC',0,1,1),
 ('82','Animal/snake bite','NC',0,1,1),
 ('83','Poisoning','NC',1,0,1),
 ('84','Liver cirrhosis','NC',0,1,1),
 ('85','Hepatocellular carcinoma','NC',0,1,1),
 ('86','Liver DISEASE s (other)','NC',0,1,1),
 ('87','Hernias','NC',0,1,1),
 ('88','DISEASEs of the appendix','NC',0,1,1),
 ('89','Musculo skeletal and connective tissue DISEASE','NC',0,1,1),
 ('9','Rabies','ND',0,1,1),
 ('90','Genito erinary system DISEASEs ( non infective )','NC',0,1,1),
 ('91','Congenital malformations and chromosome abnormalities','ND',1,0,1),
 ('92','Comlpication and surgical care','NC',0,1,1),
 ('93','Benine neoplasm''s ( all type )','NC',0,1,1),
 ('94','Cancer of the breast','NC',0,1,1),
 ('95','Cancer of the prostate','ND',1,0,1),
 ('96','Malignant neoplasm of the digestive organs','NC',0,1,1),
 ('97','Malignant of the lungs','NC',0,1,1);
INSERT INTO DISEASE (DIS_ID_A,DIS_DESC,DIS_DCL_ID_A,DIS_LOCK,DIS_OPD_INCLUDE,DIS_IPD_INCLUDE) VALUES 
 ('98','Caposis and other skin cancers','NC',0,1,1),
 ('99','Malignant neoplasm of Haemopoetic tissue','NC',0,1,1);

-- OPERATION
insert into OPERATION values ('1','OB','Caesarian section',1,0);
insert into OPERATION values ('10','OB','Septic abortion',1,0);
insert into OPERATION values ('11','OB','Dilatation and curettage',1,0);
insert into OPERATION values ('12','OB','Repair of vesico-vaginal fistula (vvf)',1,0);
insert into OPERATION values ('13','GY','Acute abdomen',1,0);
insert into OPERATION values ('14','GY','Ectopic pregnancy',1,0);
insert into OPERATION values ('15','GY','Peritonitis',1,0);
insert into OPERATION values ('16','GY','Pelvic abscess',1,0);
insert into OPERATION values ('17','GY','Uterine fibroids',1,0);
insert into OPERATION values ('18','GY','Ovarian tumours',1,0);
insert into OPERATION values ('19','GY','Uterine prolapse',1,0);
insert into OPERATION values ('2','OB','emergency',1,0);
insert into OPERATION values ('20','GY','Cystorele',1,0);
insert into OPERATION values ('21','MG','circumcision',1,0);
insert into OPERATION values ('22','MG','phimosis',1,0);
insert into OPERATION values ('23','MG','paraphimosis',1,0);
insert into OPERATION values ('24','MG','dorsal slit-paraphimosis',1,0);
insert into OPERATION values ('25','MG','uretheral stricture-bougienage',1,0);
insert into OPERATION values ('26','MG','Hydroceletomy',1,0);
insert into OPERATION values ('27','MG','Testicular tumours',1,0);
insert into OPERATION values ('28','MG','Prostatectomy',1,0);
insert into OPERATION values ('29','MG','Prostate biopsy',1,0);
insert into OPERATION values ('3','OB','elective',1,0);
insert into OPERATION values ('30','MG','Bladder biopsy',1,0);
insert into OPERATION values ('31','AG','Hernia (inguinal & femoral)',1,0);
insert into OPERATION values ('32','AG','Strangulated',1,0);
insert into OPERATION values ('33','AG','Non strangulated',1,0);
insert into OPERATION values ('34','AG','Epigastrical Hernia',1,0);
insert into OPERATION values ('37','AG','Intestinal obstruction',1,0);
insert into OPERATION values ('38','AG','Mechanical',1,0);
insert into OPERATION values ('39','AG','Volvulus',1,0);
insert into OPERATION values ('4','OB','Hysterectomy',1,0);
insert into OPERATION values ('40','AG','Laparatomy',1,0);
insert into OPERATION values ('41','AG','Penetrating abdominal injuries',1,0);
insert into OPERATION values ('42','AG','Peritonitis',1,0);
insert into OPERATION values ('43','AG','Appendicitis',1,0);
insert into OPERATION values ('44','AG','Cholecystitis',1,0);
insert into OPERATION values ('45','AG','Abdominal Tumours',1,0);
insert into OPERATION values ('46','OR','Reduction of fractures',1,0);
insert into OPERATION values ('47','OR','Upper limb',1,0);
insert into OPERATION values ('48','OR','Lower limb',1,0);
insert into OPERATION values ('49','OR','Osteomyelitis - sequestrectomy',1,0);
insert into OPERATION values ('5','OB','Ruptured uterus',1,0);	
insert into OPERATION values ('50','OS','Incission & drainage',1,0);
insert into OPERATION values ('51','OS','Debridement',1,0);
insert into OPERATION values ('52','OS','Mise -a- plat',1,0);
insert into OPERATION values ('53','OS','Surgical toilet  & suture',1,0);
insert into OPERATION values ('6','OB','Injured uterus',1,0);	
insert into OPERATION values ('8','OB','Evacuations',1,0);	
insert into OPERATION values ('9','OB','Incomplete abortion',1,0);	

-- MEDICALDSR 
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','Glucose Test Strip ( Sure Step)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Acetic Acid Glacial 1 ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Aceton 99% 1ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Copper 11 Sulphate 500g');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','EDTA Di- sodium salt 100g');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Ethanol Absolute 1ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Formaldelyde solution 35-38% 1ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Hydrochloric Acid 30-33% 1ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Iodin Crystal 100g');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Methanol 99% 1ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Phenol crystals 1kg');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Potassium iodide 100g');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Sodium Carbonate Anhydrous 500g');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Sodium Citrate 100g');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Sodium Sulphate 500g');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Sodium Nitrate 25g');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Sulphosalicylic Acid 500g');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Sulphuric Acid Conc 1ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Xylene 2.5 ltrs');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Sodium Flouride');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Potassium Oxalate');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Brillians crystal blue');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Amonium Oxalate');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','4 Dimethyl Aminobenzaldelyde');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Trichloro acetic Acid');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Non 111 Chloride (Ferric chloride)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Sodium Carbonate Anhydrous');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Trisodium Citrate');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('L','Crystal Violet');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','GPT (ALT) 200ml ( Does not have NAOH)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','GOT ( AST) 200ml has no NAOH) AS 101');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','GOT ( AST) 200ml (Calorimetric) AS 147');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','HIV 1/2 Capillus Kit 100Tests');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','HIV Buffer for determine Kit');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','HIV Determine 1/11 (Abbott) 100Tests');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','HIV UNIGOLD 1/11 Test Kits 20 Tests');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','Pregnacy ( HGG Latex) 50 tests');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','RPR 125mm x 75mm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','RPR ( VDRL Carbon ) Antigen 5ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Adrenaline 1mg/ml 1ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Aminophyline 25mg/ml,10ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Amphotericin B 50mg Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Ampicillin 500mg Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Atropine 1mg/ml 1ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Benzathine Penicillin 2.4 MIU Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Benzyl Penicillin 1 MIU Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Benzyl Penicillin 5 MIU Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Chloramphenicol 1g Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Chloroquine 40mg Base/ml 5ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Chlorpromazine 25mg/ml/2ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Cloxacillin 500mg Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Cyclophosphamide 200mg Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Cyclophosphamide 500mg Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Diazepam 5mg / ml 2ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Diclofenac 25mg/ml 3ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Digoxin 0.25 mg/ml 2ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Frusemide 10mg/ml 2ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Gentamicin 40mg/ml 2ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Haloperidol 5mg/ml 1ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Haloperidol Decanoate 50mg/ml 1ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Hydralazine 20mg Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Hydrocortisone 100mg Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Hyoscine Butyl Bromide 20mg/ml/ Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Insulin Soluble 100IU/ml 10ml Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Insulin Isophane 100IU/ml 10ml Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Insulin Mixtard 30/70 100IU/ml 10ml Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Insulin Mixtard 30/70 100IU/ml 5x3ml catridges');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Insulin Isophane 40IU/ml 10ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Iron Dextran 10mg/ml 2ml Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Ketamine 10mg/ml 20ml Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Ketamine 10mg/ml 10ml Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Lignocaine 2% 20ml Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Lignocaine 2% Adrenaline Dent.cartridges');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Lignocaine spinal 50mg/ml 2ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Methylergomatrine 0.2mg/ml 1ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Methylergomatrine 0.5mg/ml 1ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Metoclopramide 5mg/ml 100ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Metronidazole 5mg/ml 2ml IV');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Morphine 15mg/ml 1ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Oxytocin 10 IU/ml 1ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Pethidine 100mg/ml 2ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Pethidine 50mg/ml 1ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Phenobarbital 100mg/ml 2ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Phytomenadione 10mg/ml 1ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Phytomenadione 1mg/ml 1ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Procaine Penicillin Fortified 4 MIU Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Promethazine 25mg/ml 2ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Quinine Di-HCI 300mg/ml 2ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Ranitidine 25mg/ml 2ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Streptomycin 1g Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Suxamethonium 500mg Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Suxamethonium 500mg/ml 2ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','tetanus Antitoxin 1500 IU 1ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Thiopental Sodium 500mg Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Water for Injection 10ml Vial');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Sodium Chloride 0.9% IV 500ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Sodium Lactate Compound IV 500ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Acetazolamide 250mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Acyclovir 200mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Aciclovir');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Aminophylline 100mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Albendazole 400mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Albendazole 200mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Amitriptyline 25mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Amoxycillin 250mg Caps');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Amoxycillin /Clavulanate 375mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Ascorbic Acid 100mg tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Aspirin 300mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Atenolol 50mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Atenolol 100mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Bendrofluazide 5mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Benzhexol 2mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Benzhexol 5mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Bisacodyl 5mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Calcium Lactate 300mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Carbamazepine 200mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Carbimazole 5mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Charcoal 250mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Chloramphenicol 250mg Caps');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Chloroquine Uncoated 150mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Chloroquine Coated 150mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','UREA Calorimetric 300 Tests');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Chlorphenimine 4mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Chlorpromazine 100mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Chlorpromazine 25mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Cimetidine 200mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Cimetidine 400mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Ciprofloxacine 500mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Ciprofloxacine 250mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Cloxacillin 250mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Codein Phosphate 30mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Cotrimoxazole 100/20mg Paed Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Cotrimoxazole 400/80mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Darrows Half Strength 500ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Dexamethasone 4mg/ml 2ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Dexamethasone 4mg/ml 1ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Dextrose 5% IV 500ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Dextrose 30% IV 100ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Dextrose 50% IV 100ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Dexamethasone 0.5mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Diazepam 5mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Diclofenac 50mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Diethylcarbamazine 50mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Digoxin 0.25 mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Doxycycline 100mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Ephedrine 30mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Erythromycin 250mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Fansider 500/25mg Tab (50dosesx3)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Fansider 500/25mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Ferrous Sulphate 200mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Fluconazole 100mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Fluconazole 100mg 24 Caps');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Folic Acid 1mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Folic Acid 5mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Folic Acid/Ferrous Sulp 0.5/200mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Folic Acid 15mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Frusemide 40mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Glibeclamide 5mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Griseofulvin 500mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Haloperidol 5mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Haloperdol 5mg');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Hydralazine 25mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Hyoscine 10mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Ibuprofen 200mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Impramine 25mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Indomethacin 25mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Isoniazid 300mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Ketocanazole 200mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Salbutamol 4mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Spironolactone 25mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Tolbutamide 500mg tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Vitamin A 200.000 IU Caps');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Vitamin B Complex Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Oral Rehydration Salt (ORS)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Paracetamol 120mg/5ml Syrup');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Paracetamol 120mg/5ml 100ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Quinine 100mg/5ml Syrup 100ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Alcohol 95% not denatured 20Ltrs');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Chlorhexidine/Centrimide 1.5/15% 1Ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Chlorhexidine/Centrimide 1.5/15% 5Ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Getian Violet 25g Tin');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Hydrogen Peroxide 6% 250ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Iodine Solution 2% 500ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Sodium Hypochloride solution 0.75 Ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','liquid detergent 5Ltr Perfumed');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Soap Blue Bar 550g');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Liquid detergent 20Ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Soap Powder Hand wash 5kg');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Sodium Hypochlorite solution 5Ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Betamethasone 0.1% eye/ear/nose drops');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Betamethasone 0.1% Neomycin 0.35 %eye drops 7.5ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Chloramphenicol 0.5% Eye Drops 10ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Cloramphenicol 1% Eye Ointment 3.5g');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Gentamicin 0.3% eye/ear drops 10ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Hydrocortisone 1% eye drops 5ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Tetracycline eye ointment 1% 3.5g');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Beclamethasone 50mcg Innhaler');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','Urine Test Strips 3 Parameters 100 tests');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Salbutamol solution for inhalation 5ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Salbutamol Inhaler 10ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Clotrimazole 500mg Pessaries');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Clotrimazole 100mg Pessaries');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Diazepam 2mg/ml 2.5ml Rectal Tube');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Antihaemorrhoid suppositories');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Nystatin 100.000 IU Pessaries');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Dextrose Monohydrate Apyrogen 25Kg');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Amoxycillin 125mg/5ml Powd. Susp 100ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Chloramphenicol 125mg/5ml Susp 3Ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Chloramphenicol 125mg/5ml Susp 100ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Cotrimoxazole 200+40mg/5ml Susp 3Ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Cotrimoxazole 200+40mg/5ml Susp 100ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Nystatin 500.000IU/ Susp/ Drops');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Pyridoxine 50mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Quinine 300mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Ranitidine 150mg Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Rifampicin/Isoniazid 150/100 Tab');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('D','Sodium Chloride Apyrogen 50kg');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','Field stain A and B');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','Genitian Violet');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','Neutral Red');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','Eosin Yelowish');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','Giemsa Stain');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','Anti Serum A 10ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Blood giving set Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Blood Transfer Bag 300ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Insulin Syringe 100IU with Needle G26/29');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','IV Canula G16 with Port');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','IV Canula G18 with Port');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','IV Canula G20 with Port');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','IV Canula G22 with Port');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','IV Canula G24 without Port');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','IV Canula G24 with Port');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','IV Giving set Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Needle container disposable of contaminated');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Needles Luer G20 Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Needles Luer G21 Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Needles Luer G22 Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Needles Luer G23 Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Needles Spinal G20x75-120mm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Needles Spinal G22x75-120mm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Needles Spinal G25x75-120mm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Needles Spinal G22x40mm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Scalp Vein G19 Infusion set');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Scalp Vein G21 Infusion set');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Scalp Vein G23 Infusion set');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Scalp Vein G25 Infusion set');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Syringe Feeding/Irrigation 50/60ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Syringe Luer 2ml With Needle Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Syringe Luer 10ml With Needle Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Syringe Luer 20ml With Needle Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Syringe Luer 5ml With Needle Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Airway Guedel Size 00');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Airway Guedel Size 0');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Airway Guedel Size 1');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Airway Guedel Size 2');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Airway Guedel Size 3');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Eye Pad Sterile');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Adhesive Tape 2.5cm x 5m');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Adhesive Tape 7.5cm x 5m');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','cotton Wool 500G');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Cotton Wool 200G');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Elastic Bandage 10cm x 4.5m');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Elastic Bandage 7.5cm x 4.5m');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gauze Bandage 7.5cm x 3.65-4m');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gauze Bandage 10cm x 4m');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gauze Pads Non Sterile 10cm x 10cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gauze Pads  Sterile 10cm x 10cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gauze Hydrophylic 90cm x 91cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Plaster of Paris 10cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Plaster of Paris 15cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Cathether Foley CH20 3 Way Ballon');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Cathether Foley CH8 3 Way Ballon');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Cathether Foley CH10 3 Way Ballon');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Cathether Foley CH12 3 Way Ballon');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Cathether Foley CH14 3 Way Ballon');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Cathether Foley CH16 3 Way Ballon');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Cathether Foley CH18 3 Way Ballon');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Cathether Stopper for All sizes');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Nasogastric Tube G5 (Children)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Nasogastric Tube G8 (Children)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Nasogastric Tube G6 (Children)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Nasogastric Tube G10 (Children)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Nasogastric Tube G14 (Children)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Nasogastric Tube G16 (Children)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Rectal Tube CH24');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Rectal Tube CH26');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Suction Cathether Size 6 Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Suction Cathether Size 8 Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Suction Cathether Size 16 Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Suction Cathether Size 12 Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Suction Cathether Size 14 Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Suction Cathether Size 10 Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gloves Domestic');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gloves High risk non sterile Medium');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gloves Gynaecological 7.5-8');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gloves Non Sterile Medium Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gloves Non Sterile Large Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gloves Surgical Sterile 6');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gloves Surgical Sterile 6.5');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gloves Surgical Sterile 7');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gloves Surgical Sterile 7.5');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gloves Surgical Sterile 8');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gloves Surgical Sterile 8.5');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Tongue depressor Disposable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Bedpan Plastic Autoclavable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Bedpan Stainless Steel');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Body Bag 70 x 215cm  (Adult)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Bowl Without Lid 7 x 12cm stainless steel');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Bowl Without Lid 8 x 16cm stainless steel');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Bowl Without Lid 10 x 24cm stainless steel');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Air ring set 43x15cm, rubber with pump');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Colostomy Bag closed 30mm size 2');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Colostomy Bag closed 30mm size 3');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Colostomy Bag open re-usable 35mm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Ear syringe rubber 60ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','First Aid kit');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gallipot stainless steel 300ml/15cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Gallipot stainless steel 200ml/10cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Hot water Bottle 2Ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Instrument Box With Lid 20x10x5cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Instrument Tray 30 x 20 x 2cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Irrigation can with acsessories');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Kidney Dish stainless Steel 24cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Kidney Dish stainless Steel 20cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Kidney Dish Polypropylene 24cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Mackintosh Plastic (Apron) per 1m');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Mackintosh Rubber Brown (sheeting) per 1m');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Measuring Cup Graduated 25ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Nesk Support Small');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Neck Support Medium');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Neck Support Large');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Spoon Medicine 5ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Apron Plastic Re-usable');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Apron Plastic Re-usable local');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Apron Polythene Disp Non Sterile');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Razor Blades Disposable 5pc');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Stethoscope Foetal Metal');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Stethoscope Foetal Wood');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Surgical Brush (Scrubbing)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Surgical Mop 12 x 15');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Tablet Counting Tray');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Toilet Paper Rolls');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Traction Kit Children');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Traction Kit Adult');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Thermometer Clinical Flat Type');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Thermometer Clinical Prismatic Type');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Umbilical Cord Tie non sterile 100m');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Umbilical Cord Tie sterile 22m');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Urinal 1Ltr / 2Ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Urine Collecting Bag sterile 2Ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Insectcide Spray 400g');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Mosquito Net Impregnated Medium');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Mosquito Net Impregnated Large');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Mosquito Net Non Impregnated Medium');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Mosquito Net Non Impregnated Large');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Mosquito Net Impregnation Tablet');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Mosquito Net Impregnation Liquid 500ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Mosquito Wal spray Powder 80g');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Handle for surgical blade No 3');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Handle for surgical blade No 4');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Surgical Blades No 20');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Surgical Blades No 21');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Surgical Blades No 22');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Surgical Blades No 23');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Needle suture No 5 round');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Needle suture No 5 cutting');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Needle suture No 6 Round');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Suture Cutgut Chromic (0)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Suture Cutgut Chromic (2) RN22240TH');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Suture Cutgut Chromic (2/0) RN22230TH');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Suture Cutgut Chromic (3/0) RN2325TF');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Suture Cutgut Plain (2/0) RN1230TF');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Suture Silk (1) S595');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Suture Silk (2/0) RN5230TF');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Suture PGA (3/0) RN3330TF');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Lead Apron 100cmx60cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','X-Ray Developer 2.6kg for 22.5Ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','X-Ray Film 18x24cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','X-Ray Film 20x40cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','X-Ray Film 24x30cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','X-Ray Film 30x40cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','X-Ray Film 35x35cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','X-Ray Film 43x35cm');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','X-Ray Film Cassette  18x24cm with screen');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','X-Ray Film Cassette  24x30cm with screen');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','X-Ray Film Cassette  30x40cm with screen');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','X-Ray Film Cassette  35x35cm with screen');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','X-Ray Film Cassette  35x43cm with screen');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','X-Ray Fixer 3.3kg for 22.5 Ltr');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Barium Sulphate for X-Ray 1kg');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('S','Diatrizoate Meglumin Sod 76% 20ml Amp');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','Anti Serum B 10ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','Anti Serum AB 10ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','Anti Serum D 10ml');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','Creatinine 200ml (Calorimetric)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','Glucose GOD PAD  6 x 100ml (Colorimetric)');
insert into MEDICALDSR (MDSR_MDSRT_ID_A,MDSR_DESC) values ('K','Glucose Test Strips (Hyloguard)');

-- WARD
insert into WARD (WRD_ID_A ,WRD_NAME ,WRD_TELE ,WRD_FAX ,WRD_EMAIL ,WRD_NBEDS ,WRD_NQUA_NURS ,WRD_NDOC) values ('M','MATERNITY','234/52544','54324/5424','maternity@stluke.org',20,3,2);
insert into WARD (WRD_ID_A ,WRD_NAME ,WRD_TELE ,WRD_FAX ,WRD_EMAIL ,WRD_NBEDS ,WRD_NQUA_NURS ,WRD_NDOC) values ('N','NURSERY','234/52544','54324/5424','nursery@stluke.org',20,3,2);
insert into WARD (WRD_ID_A ,WRD_NAME ,WRD_TELE ,WRD_FAX ,WRD_EMAIL ,WRD_NBEDS ,WRD_NQUA_NURS ,WRD_NDOC) values ('S','SURGERY','234/52544','54324/5424','surgery@stluke.org',20,3,2);
insert into WARD (WRD_ID_A ,WRD_NAME ,WRD_TELE ,WRD_FAX ,WRD_EMAIL ,WRD_NBEDS ,WRD_NQUA_NURS ,WRD_NDOC) values ('I','INTERNAL MEDICINE','234/52544','54324/5424','internal.medicine@stluke.org',20,3,2);