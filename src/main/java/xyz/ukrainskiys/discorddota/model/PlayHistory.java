package xyz.ukrainskiys.discorddota.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayHistory {

  private Long trackId;
  private String trackUrl;
  private LocalDateTime startTime;
  private Long discordServerId;

  @Getter
  @Setter
  @Builder
  public static class DTO {
    private String name;
    private String url;
    private LocalDateTime startTime;
  }
}
