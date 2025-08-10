-- Insert into media table
INSERT INTO media (id, public_id, secure_url, created_date) 
VALUES
(6,  'giajivtnnqhnd2unm4kh', 'https://res.cloudinary.com/duss3fi7q/image/upload/v1721589424/giajivtnnqhnd2unm4kh.jpg', NOW()),
(9,  'mwzdstxipkxtd5wwnu5w', 'https://res.cloudinary.com/duss3fi7q/image/upload/v1721589553/mwzdstxipkxtd5wwnu5w.jpg', NOW()),
(10, 'ls0les0iakewdesa8ooe', 'https://res.cloudinary.com/duss3fi7q/image/upload/v1721589505/ls0les0iakewdesa8ooe.jpg', NOW()),
(11, 'xsqjovng3tfp4q75atcj', 'https://res.cloudinary.com/duss3fi7q/image/upload/v1738344874/xsqjovng3tfp4q75atcj.jpg', NOW()),
(12, 'sisuygzvher7xfvadzzc', 'https://res.cloudinary.com/duss3fi7q/image/upload/v1737998149/sisuygzvher7xfvadzzc.gif', NOW()),
(13, 'nv5gfbkmywtzpolgfzpw', 'https://res.cloudinary.com/duss3fi7q/image/upload/v1737999688/nv5gfbkmywtzpolgfzpw.jpg', NOW()),
(18, 'mrrkxxkqtslhid492ger', 'https://res.cloudinary.com/duss3fi7q/image/upload/v1738490600/mrrkxxkqtslhid492ger.jpg', NOW()),
(19, 'al7quwuwrdajy6bndcla', 'https://res.cloudinary.com/duss3fi7q/image/upload/v1738490613/al7quwuwrdajy6bndcla.jpg', NOW()),
(20, 'qixlulfimwnwxlklvus5', 'https://res.cloudinary.com/duss3fi7q/image/upload/v1738491161/qixlulfimwnwxlklvus5.jpg', NOW()),
(21, 'exxhu2fglrlwrmlfqiwo', 'https://res.cloudinary.com/duss3fi7q/image/upload/v1738491183/exxhu2fglrlwrmlfqiwo.jpg', NOW());


-- Insert into user table
INSERT INTO user (id, bio, created_date, modified_date, address, birthdate, email, hashed_password, name, phone, cover_id, image_id, is_active, is_deleted, is_enabled, is_locked)
VALUES
(1, 'kotori lover', '2024-07-17 00:56:27.477346', '2025-01-09 05:39:35.874801', '26 Nguyen Thi Thap, district 7, Ho Chi Minh city', '2003-11-11', 'minewaku@gmail.com', '$2a$10$iOqC6fdAYJAvOXVAgKMeWu/U2b43ccwU9eLrDuJVMKe7srtJvntT2', 'minewaku', '0828007686', 6, 11, 1, 0, 1, 0),
(2, 'rabbit enjoyer', '2024-07-17 00:56:56.453268', '2025-02-01 10:17:43.969821', 'In our heart!', '2003-01-01', 'admin@gmail.com', '$2a$10$rTClsoJ5ok.ODMpMXwsDPOrnHBG4plZiEHPPOFuzDKU8cO/nP9B1G', 'hakuba', '0828007686', 9, 10, 1, 0, 1, 0),
(4, 'king of pedo', '2024-12-18 16:32:40.888840', '2025-01-31 07:44:54.382655', 'Yuru camp!', '2024-12-18', 'nadeshiko@gmail.com', '$2a$10$8GS2pVgRAnMCbDKwUNFegua7bcIO6XCwsgpk0NinZrQvjj56iIlVW', 'nadeshiko', '0828007686', 12, 13, 1, 0, 1, 0),
(37, 'gay lord', '2025-02-02 10:02:40.162706', '2025-02-02 10:11:48.997074', 'Chattiniiii!', '2003-11-10', 'raora@gmail.com', '$2a$10$XgXrM84zbwMgf2ZqbXPMROxvl7xBHv6MCk9IRZCfRdAcSDqnt65vK', 'raora', '0828007686', 18, 19, 1, 1, 0, 0),
(38, 'your mom', '2025-02-02 10:06:21.499809', '2025-02-02 10:13:03.786272', 'Little nightmare ', '2003-11-10', 'mainguyenquocdung2003@gmail.com', '$2a$10$pDdyT8/fMSy0UDPGAFaUoe1H7gKWCLyLPJveKqthnJtwZLb0o8bLK', 'nimi', '0828007686', 20, 21, 1, 0, 0, 0);

-- Insert into role table
INSERT INTO role (id, created_date, modified_date, name, description)
VALUES
(1, '2024-06-28 14:43:16.000000', NULL, 'USER', 'Standard user with access to core social features.'),
(2, '2024-06-28 14:43:16.000000', NULL, 'ADMIN', 'Administrator with full access to system management, excluding role and permission configurations.'),
(3, '2024-06-28 14:43:16.000000', NULL, 'SYSTEM', 'System owner with the highest level of control, including role and permission management.'),
(4, '2024-06-28 14:43:16.000000', NULL, 'MODERATOR', 'Responsible for monitoring content, managing user activities, and enforcing community guidelines.'),
(5, '2024-06-28 14:43:16.000000', NULL, 'BANNED', 'User with no permissions assigned; fully restricted from accessing any system features.');

-- Insert into permission table
INSERT INTO permission (id, created_date, modified_date, name, description, is_deleted)
VALUES
(1, '2024-06-28 14:48:18.000000', NULL, 'USER_READ', 'read user', 0),
(2, '2024-06-28 14:48:18.000000', NULL, 'USER_UPDATE', 'update user', 0),
(3, '2024-06-28 14:48:18.000000', NULL, 'USER_DELETE', 'delete user', 0),
(4, '2024-06-28 14:48:18.000000', NULL, 'USER_CREATE', 'create user', 0),
(5, '2024-06-28 14:48:18.000000', NULL, 'ROLE_READ', 'read role', 0),
(6, '2024-06-28 14:48:18.000000', NULL, 'ROLE_UPDATE', 'update role', 0),
(7, '2024-06-28 14:48:18.000000', NULL, 'ROLE_DELETE', 'delete role', 0),
(8, '2024-06-28 14:48:18.000000', NULL, 'ROLE_CREATE', 'create role', 0),
(9, '2024-06-28 14:48:18.000000', NULL, 'PERMISSION_READ', 'read permission', 0),
(10, '2024-06-28 14:48:18.000000', NULL, 'PERMISSION_UPDATE', 'update permission', 0),
(11, '2024-06-28 14:48:18.000000', NULL, 'PERMISSION_DELETE', 'delete permission', 0),
(12, '2024-06-28 14:48:18.000000', NULL, 'PERMISSION_CREATE', 'create permission', 0);

-- Insert into user_role table
INSERT INTO user_role (user_id, role_id, created_by_id, created_date)
VALUES
(1, 1, 2, '2024-06-28 14:48:18.000000'),
(2, 1, 1, '2024-06-28 14:48:18.000000'),
(4, 1, 1, '2024-06-28 14:48:18.000000'),
(37, 1, 1, '2024-06-28 14:48:18.000000'),
(38, 1, 1, '2024-06-28 14:48:18.000000'),
(4, 2, 1, '2024-06-28 14:48:18.000000');

-- Insert into role_permission table
INSERT INTO role_permission (role_id, permission_id, created_by_id, created_date) 
VALUES
(1, 1, 1, '2024-06-28 14:48:18.000000'),
(1, 2, 1, '2024-06-28 14:48:18.000000'),
(1, 3, 1, '2024-06-28 14:48:18.000000'),
(1, 4, 1, '2024-06-28 14:48:18.000000'),
(2, 1, 1, '2024-06-28 14:48:18.000000'),
(2, 2, 1, '2024-06-28 14:48:18.000000'),
(2, 3, 1, '2024-06-28 14:48:18.000000'),
(2, 4, 1, '2024-06-28 14:48:18.000000'),
(2, 5, 1, '2024-06-28 14:48:18.000000'),
(2, 6, 1, '2024-06-28 14:48:18.000000'),
(2, 7, 1, '2024-06-28 14:48:18.000000'),
(2, 8, 1, '2024-06-28 14:48:18.000000'),
(2, 9, 1, '2024-06-28 14:48:18.000000'),
(2, 10, 1, '2024-06-28 14:48:18.000000'),
(2, 11, 1, '2024-06-28 14:48:18.000000'),
(2, 12, 1, '2024-06-28 14:48:18.000000');

-- Insert into post table
INSERT INTO post (id, content, user_id, like_count, view_count, comment_count, status, lat, lon, created_date)
VALUES
(3, 'It fxxking GLOWS...!:v SHINY...!:v GOLDEN...! \n:v /Ah yes the level uncap glow strikes again...:v ', 37, 0, 0, 0, 1, 0, 0, '2025-04-30 08:59:58.214065'),
(2, 'Chiến Thần Lạc Hồng vừa ra mắt thời gian gần đây và nhanh chóng lập tức tạo tiếng vang trong lòng khán giả Việt Nam và bạn bè quốc tế. Bộ phim lần đầu kết hợp yếu tố hành động - viễn tưởng theo phong cách Tokusatsu với chất liệu văn hóa dân gian Việt Nam đã thu về một lượng lớn người xem trên YouTube.', 2, 0, 0, 0, 0, 0, 0, '2025-04-30 08:49:23.748666'),
(1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque commodo enim lacus, non mattis lectus ornare eu. Vivamus eleifend tellus quis urna cursus, at ornare nisl pellentesque. Duis pellentesque lobortis erat. Mauris rhoncus mauris ex, ac dictum nunc congue vulputate. Donec vel rutrum arcu. Aenean blandit aliquet laoreet. Praesent augue ligula, consectetur a tellus sed, elementum pulvinar dolor. Maecenas ultrices gravida ante', 1, 0, 0, 0, 0, 12, 12, '2025-04-30 08:14:45.723467');

-- TRIGGERS
-- Trigger to increment like_count after a like is added
CREATE TRIGGER update_like_count_after_insert
AFTER INSERT ON `like`
FOR EACH ROW
BEGIN
    UPDATE post
    SET like_count = like_count + 1
    WHERE id = NEW.post_id;
END;

-- Trigger to decrement like_count after a like is removed
CREATE TRIGGER update_like_count_after_delete
AFTER DELETE ON `like`
FOR EACH ROW
BEGIN
    UPDATE post
    SET like_count = like_count - 1
    WHERE id = OLD.post_id;
END;

-- Trigger to increment comment_count after a comment is added
CREATE TRIGGER update_comment_count_after_insert
AFTER INSERT ON `comment`
FOR EACH ROW
BEGIN
    UPDATE post
    SET comment_count = comment_count + 1
    WHERE id = NEW.post_id;
END;

-- Trigger to decrement comment_count after a comment is removed
CREATE TRIGGER update_comment_count_after_delete
AFTER DELETE ON `comment`
FOR EACH ROW
BEGIN
    UPDATE post
    SET comment_count = comment_count - 1
    WHERE id = OLD.post_id;
END;



