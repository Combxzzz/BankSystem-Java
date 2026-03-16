package model.enums;

public enum AccountStatus {
    ACTIVE(true),
    BLOCKED(false),
    CLOSED(false);

    private final boolean canTransact;

    AccountStatus(boolean canTransact) {
        this.canTransact = canTransact;
    }

    public boolean canTransact() {
        return canTransact;
    }

    public boolean canChangeTo(AccountStatus newStatus) {
        return switch (this) {
            case ACTIVE -> newStatus == BLOCKED || newStatus == CLOSED;
            case BLOCKED -> newStatus == ACTIVE || newStatus == CLOSED;
            case CLOSED -> false;
        };
    }
}
