package xyz.ukrainskiys.discorddota.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscordServer {

  private Long id;
  private Long voiceChannelId;
}
