package xyz.ukrainskiys.discorddota.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayHistory {

  public Long trackId;
  public String trackUrl;
  public LocalDateTime startTime;
  public Long discordServerId;
}
