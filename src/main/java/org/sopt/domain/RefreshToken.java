package org.sopt.domain;

@Getter
public class RefreshToken {
    private static final int EXPIRY_DAYS = 7;
    private static final int MIN_TOKEN_LENGTH = 10;

    public static RefreshToken create(Long userId, String token);
    public boolean isExpired()
    public void use()
    public boolean belongsTo(Long userId)
}