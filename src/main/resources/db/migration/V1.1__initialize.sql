SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO resource (resource_id, label, created_by, created_date, last_modified_by, last_modified_date)
VALUES('account', '帳戶服務', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);

-- ----------------------------
-- Records of resource_scop
-- ----------------------------
INSERT INTO resource_scop (scop_id, resource_id, scop_code, label, created_by, created_date, last_modified_by, last_modified_date)
VALUES('0001', 'account', 'account', '帳號讀寫', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO resource_scop (scop_id, resource_id, scop_code, label, created_by, created_date, last_modified_by, last_modified_date)
VALUES('0002', 'account', 'account.readonly', '帳號唯讀', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);

INSERT INTO resource_scop (scop_id, resource_id, scop_code, label, created_by, created_date, last_modified_by, last_modified_date)
VALUES('0003', 'account', 'role', '角色讀寫', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO resource_scop (scop_id, resource_id, scop_code, label, created_by, created_date, last_modified_by, last_modified_date)
VALUES('0004', 'account', 'role.readonly', '角色唯讀', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO oauth_client_details
(client_id, client_secret, web_server_redirect_uri, access_token_validity, refresh_token_validity, auto_approve, created_by, created_date, last_modified_by, last_modified_date)
VALUES('app', '$2a$10$c85hYXPx4niZCCkmxeqXHOriQvvaWBSd9SVpYoq2ZAbs0uUa1ESL.', NULL, 86400, 604800, 1, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
-- default password is 123456

-- ----------------------------
-- Records of oauth_client_grant_types
-- ----------------------------
INSERT INTO oauth_client_grant_types (sid, client_id, grant_type, created_by, created_date, last_modified_by, last_modified_date)
VALUES('0001', 'app', 'password', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO oauth_client_grant_types (sid, client_id, grant_type, created_by, created_date, last_modified_by, last_modified_date)
VALUES('0002', 'app', 'refresh_token', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);

-- ----------------------------
-- Records of oauth_client_resource_map
-- ----------------------------
INSERT INTO oauth_client_resource_map (sid, client_id, resource_id, created_by, created_date, last_modified_by, last_modified_date)
VALUES('0001', 'app', 'account', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);

-- ----------------------------
-- Records of role_info
-- ----------------------------
INSERT INTO `role_info` (role_id, code, label, created_by, created_date, last_modified_by, last_modified_date)
VALUES('0001', 'admin', '系統管理員', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);

-- ----------------------------
-- Records of role_scop
-- ----------------------------
INSERT INTO role_scop (ser_id, role_id, scop_id, created_by, created_date, last_modified_by, last_modified_date)
VALUES('0001', '0001', '0001', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
INSERT INTO role_scop (ser_id, role_id, scop_id, created_by, created_date, last_modified_by, last_modified_date)
VALUES('0002', '0001', '0003', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);

-- ----------------------------
-- Records of user_account
-- ----------------------------
INSERT INTO user_account (uid, account, password, enabled, expired, locked, credentials_expired, created_by, created_date, last_modified_by, last_modified_date)
VALUES('0001', 'admin', '$2a$10$c85hYXPx4niZCCkmxeqXHOriQvvaWBSd9SVpYoq2ZAbs0uUa1ESL.', 1, 0, 0, 0, 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
-- default password is 123456

-- ----------------------------
-- Records of account_role_map
-- ----------------------------
INSERT INTO user_role_map (sid, uid, role_id, created_by, created_date, last_modified_by, last_modified_date)
VALUES('0001', '0001', '0001', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);

-- ----------------------------
-- Records of user_profile
-- ----------------------------
INSERT INTO user_profile (uid, email, username, created_by, created_date, last_modified_by, last_modified_date)
VALUES('0001', '5566@gmail.com', 'Sam', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);


SET FOREIGN_KEY_CHECKS=1;
