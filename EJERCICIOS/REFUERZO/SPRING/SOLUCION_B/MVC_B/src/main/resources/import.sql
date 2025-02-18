INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO users(email,password,username) VALUES('admin@admin.es','$2a$10$sTdQgzZPWUfHhyR5Q1aGP.f1smPH6z05JBuEHedX7wBw92umcwhqm','admin');
INSERT INTO users(email,password,username) VALUES('user@user.es','$2a$10$DKpwOn88Oax3e/qnxVtmcOdNoNAwSloyXWWd4KXRYU0012eB.JL1W','user');

INSERT INTO user_roles(role_id,user_id) VALUES(1,2);
INSERT INTO user_roles(role_id,user_id) VALUES(2,1);

