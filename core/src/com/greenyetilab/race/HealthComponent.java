package com.greenyetilab.race;

/**
 * Created by aurelien on 19/12/14.
 */
public class HealthComponent {
    public static final float DYING_DURATION = 0.5f;
    private static final float INVULNERABILITY_INTERVAL = 0.5f;

    private float mInvulnerabilityTimer;
    private int mOldHealth = 1; // Used to detect health decrease in act()
    private int mHealth = 1;
    private int mMaxHealth = 1;
    private float mKilledTime = 0;

    boolean act(float dt) {
        if (mInvulnerabilityTimer > 0) {
            mInvulnerabilityTimer -= dt;
        }
        if (mOldHealth > mHealth) {
            mOldHealth = mHealth;
            onHealthDecreased();
        }
        if (mHealth == 0) {
            if (mKilledTime == 0) {
                onJustDied();
            }
            mKilledTime += dt;
        }
        return true;
    }

    public State getState() {
        if (mHealth > 0) {
            return State.ALIVE;
        }
        return mKilledTime < DYING_DURATION ? State.DYING : State.DEAD;
    }

    public float getKilledTime() {
        return mKilledTime;
    }

    public int getHealth() {
        return mHealth;
    }

    public int getMaxHealth() {
        return mMaxHealth;
    }

    public void setInitialHealth(int health) {
        mOldHealth = health;
        mHealth = health;
        mMaxHealth = health;
    }

    public void decreaseHealth() {
        if (mInvulnerabilityTimer > 0) {
            return;
        }
        mInvulnerabilityTimer = INVULNERABILITY_INTERVAL;
        mHealth--;
    }

    public void kill() {
        if (mInvulnerabilityTimer > 0) {
            return;
        }
        mHealth = 0;
    }

    protected void onHealthDecreased() {
    }

    protected void onJustDied() {
    }

    public static enum State {
        ALIVE,
        DYING,
        DEAD
    }
}
