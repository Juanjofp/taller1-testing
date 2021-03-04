package es.centic.taller1.taller1.users;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class AvatarService {
    public static final String API = "https://gameserver.centic.ovh";

    
    private WebClient webClient;

    public AvatarService(WebClient webclient) {
        this.webClient = webclient;
    }

    public AvatarService(String url) {
        webClient = WebClient.create(url);
    }

    public AvatarService() {
        webClient = WebClient.create(API);
    }

    public void setWebclient(WebClient webclient) {
        this.webClient = webclient;
    }

    public String cacheAvatar(@Nullable String avatar) {
        if(avatar  == null) {
            Mono<ApiImage> response = webClient.get().uri("/avatars").retrieve().bodyToMono(ApiImage.class);
            ApiImage image = response.block();
            return image.getImage();
        }
        return avatar;
    }
}
