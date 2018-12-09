package net.alicmp.android.cardzilla.model;

/**
 * Created by ali on 3/19/16.
 */
public class PacketStatistics {

    private int reviewingCardsCount;
    private int newCardsCount;
    private int learningCardsCount;

    public PacketStatistics() {}

    public PacketStatistics(int reviewingCardsCount, int newCardsCount, int learningCardsCount) {

        this.reviewingCardsCount = reviewingCardsCount;
        this.newCardsCount = newCardsCount;
        this.learningCardsCount = learningCardsCount;
    }

    public int getReviewingCardsCount() {
        return reviewingCardsCount;
    }

    public void setReviewingCardsCount(int reviewingCardsCount) {
        this.reviewingCardsCount = reviewingCardsCount;
    }

    public int getNewCardsCount() {
        return newCardsCount;
    }

    public void setNewCardsCount(int newCardsCount) {
        this.newCardsCount = newCardsCount;
    }

    public int getLearningCardsCount() {
        return learningCardsCount;
    }

    public void setLearningCardsCount(int learningCardsCount) {
        this.learningCardsCount = learningCardsCount;
    }

    public boolean isPacketEmpty() {
        return (getLearningCardsCount() + getNewCardsCount() + getReviewingCardsCount()) == 0;
    }
}
