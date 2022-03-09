package com.techyourchance.testdrivendevelopment.exercise7;

interface GetReputationUseCaseSync {

    UseCaseResult getReputation();

    class UseCaseResult {
        private final Status mStatus;
        private final int reputation;

        public UseCaseResult(Status mStatus, int reputation) {
            this.mStatus = mStatus;
            this.reputation = reputation;
        }

        public Status getStatus() {
            return mStatus;
        }

        public int getReputation() {
            return reputation;
        }

    }

     enum Status {FAILURE, SUCCESS}
}
