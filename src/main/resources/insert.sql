INSERT INTO accounts
VALUES
    (false, NOW(), NULL, 1, NOW(), 'PASSWORD', 'user1@naver.com', '1234', '1', 'USER'),
    (false, NOW(), NULL, 2, NOW(), 'PASSWORD', 'user2@naver.com', '1234', '1', 'USER');



INSERT INTO categories (type) VALUES
                                  ('KPOP'),
                                  ('ENTERTAINMENT'),
                                  ('DRAMA'),
                                  ('INFLUENCER'),
                                  ('NEWS'),
                                  ('MOVIE'),
                                  ('ANIMATION'),
                                  ('CHALLENGE'),
                                  ('NEW_SLANG'),
                                  ('TRENDING'),
                                  ('ETC'),
                                  ('USER_CONTENTS');


INSERT INTO user_profiles (
    id,
    account_id,
    nickname,
    bio,
    profileUrl,
    accountvisible,
    followervisible,
    created_at,
    updated_at
) VALUES
      (1, 1,'철수', 'Software Developer at TechCorp', 'https://example.com.jpg', true, true, NOW(), NOW()),
      (2,2, '훈이', 'Designer & Creative Director', 'https://example.com.jpg', true, false, NOW(), NOW());




INSERT INTO posts (id, title, media_url, media_upload_time, account_id, category_id, createdat, updatedat, deletedat, isdeleted)
VALUES
    (2, 'Title 2', 'https://example.com/media2.mp4', '2025-06-01 10:01:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (3, 'Title 3', 'https://example.com/media3.mp4', '2025-06-01 10:02:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (4, 'Title 4', 'https://example.com/media4.mp4', '2025-06-01 10:03:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (5, 'Title 5', 'https://example.com/media5.mp4', '2025-06-01 10:04:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (6, 'Title 6', 'https://example.com/media6.mp4', '2025-06-01 10:05:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (7, 'Title 7', 'https://example.com/media7.mp4', '2025-06-01 10:06:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (8, 'Title 8', 'https://example.com/media8.mp4', '2025-06-01 10:07:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (9, 'Title 9', 'https://example.com/media9.mp4', '2025-06-01 10:08:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (10, 'Title 10', 'https://example.com/media10.mp4', '2025-06-01 10:09:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (11, 'Title 11', 'https://example.com/media11.mp4', '2025-06-01 10:10:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (12, 'Title 12', 'https://example.com/media12.mp4', '2025-06-01 10:11:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (13, 'Title 13', 'https://example.com/media13.mp4', '2025-06-01 10:12:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (14, 'Title 14', 'https://example.com/media14.mp4', '2025-06-01 10:13:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (15, 'Title 15', 'https://example.com/media15.mp4', '2025-06-01 10:14:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (16, 'Title 16', 'https://example.com/media16.mp4', '2025-06-01 10:15:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (17, 'Title 17', 'https://example.com/media17.mp4', '2025-06-01 10:16:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (18, 'Title 18', 'https://example.com/media18.mp4', '2025-06-01 10:17:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (19, 'Title 19', 'https://example.com/media19.mp4', '2025-06-01 10:18:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (20, 'Title 20', 'https://example.com/media20.mp4', '2025-06-01 10:19:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (21, 'Title 21', 'https://example.com/media21.mp4', '2025-06-01 10:20:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (22, 'Title 22', 'https://example.com/media22.mp4', '2025-06-01 10:21:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (23, 'Title 23', 'https://example.com/media23.mp4', '2025-06-01 10:22:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (24, 'Title 24', 'https://example.com/media24.mp4', '2025-06-01 10:23:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (25, 'Title 25', 'https://example.com/media25.mp4', '2025-06-01 10:24:00', 1, 1, NOW(), NOW(),NOW(),FALSE),
    (26, 'Title 26 입니다', 'https://example.com/media25.mp4', '2025-06-01 10:24:00', 1, 1, NOW(), NOW(),NOW(),FALSE);