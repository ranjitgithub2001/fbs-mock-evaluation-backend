package com.fbs.mock_evaluation_system.dto;

public class SuggestedStageDTO {

    private Long mockStageId;
    private String stageName;
    private Integer stageSequence;

    public SuggestedStageDTO(Long mockStageId, String stageName, Integer stageSequence) {
        this.mockStageId = mockStageId;
        this.stageName = stageName;
        this.stageSequence = stageSequence;
    }

    public Long getMockStageId() { return mockStageId; }
    public String getStageName() { return stageName; }
    public Integer getStageSequence() { return stageSequence; }
}