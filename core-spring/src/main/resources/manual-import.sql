-- system administrator
INSERT INTO USER_ACCOUNTS (ID, EMAIL, PASSWORD, USERNAME ) VALUES  ( 1, 'adaptserver@gmail.com', 'adapt2010', 'Administrator' );
INSERT INTO USER_ACCOUNTS_PRIVILEGES (USER_ACCOUNT, PRIVILEGE ) VALUES  ( 1, 'ROLE_ADMINISTRATOR' );
INSERT INTO USER_ACCOUNTS_PRIVILEGES (USER_ACCOUNT, PRIVILEGE ) VALUES  ( 1, 'ROLE_TEACHER' );
