package xyz.ukrainskiys.discorddota.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Track {

  private Long id;
  private String title;
  private String url;
  private Long discordServerId;
}
