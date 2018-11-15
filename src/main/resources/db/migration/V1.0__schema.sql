SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for resource & scop
-- ----------------------------
CREATE TABLE `resource` (
  `resource_id` varchar(50) NOT NULL COMMENT '資源id',
  `label` varchar(236) DEFAULT NULL COMMENT '說明標籤',
  `created_by` varchar(100) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` varchar(100) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='資源列表';

CREATE TABLE `resource_scop` (
  `scop_id` varchar(36) NOT NULL COMMENT '亂數產生',
  `resource_id` varchar(50) NOT NULL COMMENT '對應的資源ID',
  `scop_code` varchar(50) NOT NULL COMMENT '功能代碼',
  `label` varchar(50) NOT NULL COMMENT '說明標籤文字',
  `created_by` varchar(100) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` varchar(100) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`scop_id`),
  UNIQUE KEY `resource_scop_uk_1` (`scop_code`),
  CONSTRAINT `resource_scop_resource_id_fk_1` FOREIGN KEY (`resource_id`) REFERENCES `resource` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='資源範圍';

-- ----------------------------
-- Table structure for oauthClient
-- ----------------------------
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(50) NOT NULL COMMENT '指定OAuth2 client ID',
  `client_secret` varchar(256) NOT NULL COMMENT '指定OAuth2 client secret',
  `web_server_redirect_uri` varchar(256) DEFAULT NULL COMMENT '服務端pre-established的跳轉URI',
  `access_token_validity` int(11) DEFAULT NULL COMMENT '指定access token失效時長',
  `refresh_token_validity` int(11) DEFAULT NULL COMMENT '指定refresh token的有效期',
  `auto_approve` bit(1) NOT NULL COMMENT '是否自動授權相關範圍',
  `created_by` varchar(100) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` varchar(100) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='授權客戶端';

CREATE TABLE `oauth_client_grant_types` (
  `sid` varchar(36) NOT NULL COMMENT '流水號',
  `client_id` varchar(50) NOT NULL COMMENT '指定OAuth2 client ID',
  `grant_type` varchar(256) NOT NULL COMMENT '指定獲取資源的access token的授權類型',
  `created_by` varchar(100) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` varchar(100) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`sid`),
  UNIQUE KEY `oauth_client_grant_types_uk1` (`client_id`,`grant_type`),
  CONSTRAINT `oauth_client_grant_types_client_id_fk_1` FOREIGN KEY (`client_id`) REFERENCES `oauth_client_details` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客戶端可用授權方式';

CREATE TABLE `oauth_client_resource_map` (
  `sid` varchar(36) NOT NULL COMMENT '流水號',
  `client_id` varchar(50) NOT NULL COMMENT '指定OAuth2 client ID',
  `resource_id` varchar(50) NOT NULL COMMENT '資源id',
  `created_by` varchar(100) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` varchar(100) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`sid`),
  UNIQUE KEY `oauth_client_resource_map_uk_1` (`client_id`,`resource_id`),
  CONSTRAINT `oauth_client_resource_map_client_id_fk_1` FOREIGN KEY (`client_id`) REFERENCES `oauth_client_details` (`client_id`),
  CONSTRAINT `oauth_client_resource_map_resource_id_fk_2` FOREIGN KEY (`resource_id`) REFERENCES `resource` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客戶端可存取的資源';

-- ----------------------------
-- Table structure for role
-- ----------------------------
CREATE TABLE `role_info` (
  `role_id` varchar(36) NOT NULL COMMENT '亂數產生',
  `code` varchar(50) NOT NULL COMMENT '角色代碼',
  `label` varchar(50) NOT NULL COMMENT '角色名稱',
  `created_by` varchar(100) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` varchar(100) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色';

CREATE TABLE `role_scop` (
  `ser_id` varchar(36) NOT NULL COMMENT '亂數產生',
  `role_id` varchar(36) NOT NULL COMMENT '角色ID',
  `scop_id` varchar(36) NOT NULL COMMENT 'ScopID',
  `created_by` varchar(100) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` varchar(100) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`ser_id`),
  UNIQUE KEY `role_scop_uk_1` (`role_id`,`scop_id`),
  CONSTRAINT `role_scop_role_id_fk_1` FOREIGN KEY (`role_id`) REFERENCES `role_info` (`role_id`),
  CONSTRAINT `role_scop_scop_id_fk_2` FOREIGN KEY (`scop_id`) REFERENCES `resource_scop` (`scop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色對應權限範圍';

CREATE TABLE `user_account` (
  `uid` varchar(36) NOT NULL COMMENT '用戶代碼',
  `account` varchar(50) NOT NULL COMMENT '登入用帳號',
  `password` varchar(60) NOT NULL COMMENT '用戶密碼',
  `enabled` bit(1) NOT NULL COMMENT '是否可用',
  `expired` bit(1) NOT NULL COMMENT '是否過期',
  `locked` bit(1) NOT NULL COMMENT '帳號是否鎖定為',
  `credentials_expired` bit(1) NOT NULL COMMENT '證書是否過期',
  `created_by` varchar(100) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` varchar(100) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='使用者';

CREATE TABLE `user_role_map` (
  `sid` varchar(36) NOT NULL COMMENT '亂數產生',
  `uid` varchar(36) NOT NULL COMMENT '帳號ID',
  `role_id` varchar(36) NOT NULL COMMENT '角色ID',
  `created_by` varchar(100) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` varchar(100) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`sid`),
  UNIQUE KEY `account_role_map_uk_1` (`uid`,`role_id`),
  CONSTRAINT `account_role_map_role_id_fk_1` FOREIGN KEY (`role_id`) REFERENCES `role_info` (`role_id`),
  CONSTRAINT `account_role_map_uid_fk_1` FOREIGN KEY (`uid`) REFERENCES `user_account` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='使用者對應角色';

CREATE TABLE `user_profile` (
  `uid` varchar(36) NOT NULL COMMENT '用戶代碼',
  `email` varchar(255) NOT NULL COMMENT '信箱',
  `username` varchar(50) NOT NULL COMMENT '用戶姓名',
  `created_by` varchar(100) NOT NULL,
  `created_date` datetime NOT NULL,
  `last_modified_by` varchar(100) DEFAULT NULL,
  `last_modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `email` (`email`),
  CONSTRAINT `user_profile_uid_fk_1` FOREIGN KEY (`uid`) REFERENCES `user_account` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='使用者資料';

-- ----------------------------
-- Table structure for token
-- pk 跟 Spring 設計不一樣 由 PRIMARY KEY (`authentication_id`) 改成 PRIMARY KEY (`token_id`)
-- ----------------------------
CREATE TABLE `oauth_access_token` (
  `token_id` varchar(256) NOT NULL,
  `token` blob,
  `authentication_id` varchar(256) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  `authentication` blob,
  `refresh_token` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='核發Token列表';

CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(256) NOT NULL,
  `token` blob,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='更新Token列表';

SET FOREIGN_KEY_CHECKS=1;
