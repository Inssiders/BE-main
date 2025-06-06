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

-- 여러 개의 샘플 데이터 INSERT
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
      (1, 1,'철수', 'Software Developer at TechCorp', 'https://example.com.jpg', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
      (2,2, '훈이', 'Designer & Creative Director', 'https://example.com.jpg', true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);