-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner1','owner');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities VALUES ('vet1','veterinarian');

INSERT INTO vets VALUES (1, 'James', 'Carter');
INSERT INTO vets VALUES (2, 'Helen', 'Leary');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'owner1');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'owner1');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'owner1');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'owner1');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'owner1');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'owner1');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'owner1');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'owner1');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'owner1');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'owner1');

INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);

INSERT INTO visits(id,pet_id,vet_id,visit_date,visit_time,description) VALUES (1, 7, 1, '2013-01-01','15:30' ,'rabies shot');
INSERT INTO visits(id,pet_id,vet_id,visit_date,visit_time,description) VALUES (5, 7, 1, '2020-08-14','15:30' ,'rabies shot');
INSERT INTO visits(id,pet_id,vet_id,visit_date,visit_time,description) VALUES (6, 7, 1, '2020-09-11','13:30' ,'rabies shot');
INSERT INTO visits(id,pet_id,vet_id,visit_date,visit_time,description) VALUES (7, 7, 1, '2021-02-01','14:30' ,'rabies shot');
INSERT INTO visits(id,pet_id,vet_id,visit_date,visit_time,description) VALUES (8, 7, 1, '2021-03-14','11:30' ,'rabies shot');
INSERT INTO visits(id,pet_id,vet_id,visit_date,visit_time,description) VALUES (2, 8, 1,'2013-01-02', '15:30' ,'rabies shot');
INSERT INTO visits(id,pet_id,vet_id,visit_date,visit_time,description) VALUES (3, 8, 2,'2013-01-03', '15:30' ,'neutered');
INSERT INTO visits(id,pet_id,vet_id,visit_date,visit_time,description) VALUES (4, 7, 3,'2013-01-04', '15:30' ,'spayed');
INSERT INTO visits(id,pet_id,vet_id,visit_date,visit_time,description) VALUES (9, 1, 1, '2013-01-01','15:30' ,'rabies shot');


INSERT INTO medicines (id, name, description, side_effects) VALUES (1, 'Ibuprofeno', 'Farmaco que se usa para tratar la fiebre y/o el dolor', 'Estrenimiento, diarrea, gases y mareo');
INSERT INTO medicines (id, name, description, side_effects) VALUES (2, 'Pipetas parasital perros y gatos', 'Antiparasitario repelente contra todo tipo de insectos. Zotal', 'Generalmente se trata de vómitos leves y transitorios que no demandan tratamiento');
INSERT INTO medicines (id, name, description, side_effects) VALUES (3, 'ENACARD 28 Comprimidos', 'Tratamiento de la insuficiencia cardiaca en perros. Por insuficiencia de la válvula mitral y cardiomiopatía dilatada', 'No procede ');
INSERT INTO medicines (id, name, description, side_effects) VALUES (4, 'DEYANIL RETARD 10 ml', 'Antiinflamatorio esteroide para Perros', 'Disminución de las defensas orgánicas y retraso en la cicatrización de heridas.');
INSERT INTO medicines (id, name, description, side_effects) VALUES (5, 'TOLFEDINE 40 mg/ml 30 ml', 'Antiinflamatorio Inyectable Perros y Gatos', 'Tolfedine 4% posee excelente tolerancia como AINE de primera elección. A veces pueden aparecer vómitos y diarreas durante el tratamiento, así como poliuria y polidipsia que cesan al suspender el tratamiento.');

INSERT INTO treatments(id,start_date, recomendations, end_date, medicine_id) VALUES (1,'2021-02-27', 'reposo y tomar las medicinas dos veces al día', '2021-04-25', 1);
INSERT INTO treatments(id,start_date, recomendations, end_date, medicine_id) VALUES (2,'2020-03-01', 'operación del pulmón izquierdo', '2020-04-01', 2);
INSERT INTO treatments(id,start_date, recomendations, end_date, medicine_id) VALUES (3,'2020-03-02', 'solo reposo varios dias', '2020-03-05', 3 );
INSERT INTO treatments(id,start_date, recomendations, end_date, medicine_id) VALUES (4,'2020-03-02', 'estancia durante unos dias y tomar medicación', '2020-03-11', 4 );
INSERT INTO treatments(id,start_date, recomendations, end_date, medicine_id) VALUES (5,'2031-03-02', 'Absoluto reposo', '2032-03-11', 1 );
INSERT INTO treatments(id,start_date, recomendations, end_date, medicine_id) VALUES (6,'2030-03-02', 'No dejar de mover el pico', '2031-03-11', 1 );
INSERT INTO treatments(id,start_date, recomendations, end_date, medicine_id) VALUES (7,'2030-03-02', 'No dejar de mover el pico', '2031-03-11', 1 );


INSERT INTO residences(id, date_start, date_end) VALUES (1, '2020-02-04', '2020-02-23');
INSERT INTO residences(id, date_start, date_end) VALUES (2, '2020-01-07', '2021-02-01');
INSERT INTO residences(id, date_start, date_end) VALUES (3, '2020-03-03', '2020-03-03') ;
INSERT INTO residences(id, date_start, date_end) VALUES (4, '2019-07-12', '2020-09-18');
INSERT INTO residences(id, date_start, date_end) VALUES (5, '2020-03-12', '2022-09-18');
INSERT INTO residences(id, date_start, date_end) VALUES (6, '2019-01-12', '2022-01-18');
INSERT INTO residences(id, date_start, date_end) VALUES (7, '2020-02-15', '2021-10-11');



INSERT  INTO diagnosis (id, description,status, vet_id, treatment_id, residence_id) VALUES (1,'Broken leg', 1, 1,1,1);
INSERT  INTO diagnosis (id, description,status, vet_id, treatment_id, residence_id) VALUES (2,'Sore throat', 2, 1,2,2);
INSERT  INTO diagnosis (id, description,status, vet_id, treatment_id, residence_id) VALUES (3,'Infarct', 2, 2, 3, null);
INSERT  INTO diagnosis (id, description,status, vet_id, treatment_id, residence_id) VALUES (4,'Coronavirus', 1, 2,4, 3);
INSERT  INTO diagnosis (id, description,status, vet_id, treatment_id, residence_id) VALUES (5,'Minor injuries', 1, 2 ,5, 5);
INSERT  INTO diagnosis (id, description,status, vet_id, treatment_id, residence_id) VALUES (6,'Leg injury', 1, 4, 6, 6);
INSERT  INTO diagnosis (id, description,status, vet_id, treatment_id, residence_id) VALUES (7,'Leg injury', 1, 4, 6, 7);
INSERT  INTO diagnosis (id, description,status, vet_id, treatment_id, residence_id) VALUES (8,'The pet ate a toxic food', 2, 4, null, null);
INSERT  INTO diagnosis (id, description,status, vet_id, treatment_id, residence_id) VALUES (9,'The pet is hungry', 1, 4, null, null);





INSERT  INTO pets_diagnosis (pet_id, diagnosis_id) VALUES (1,1);
INSERT  INTO pets_diagnosis (pet_id, diagnosis_id) VALUES (1,5);
INSERT  INTO pets_diagnosis (pet_id, diagnosis_id) VALUES (1,6);
INSERT  INTO pets_diagnosis (pet_id, diagnosis_id) VALUES (2,4);
INSERT  INTO pets_diagnosis (pet_id, diagnosis_id) VALUES (3,2);
INSERT  INTO pets_diagnosis (pet_id, diagnosis_id) VALUES (4,3);
INSERT  INTO pets_diagnosis (pet_id, diagnosis_id) VALUES (7,7);
INSERT  INTO pets_diagnosis (pet_id, diagnosis_id) VALUES (13,8);
INSERT  INTO pets_diagnosis (pet_id, diagnosis_id) VALUES (2,9);
