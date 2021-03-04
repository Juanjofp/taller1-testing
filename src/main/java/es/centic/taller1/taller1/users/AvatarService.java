package es.centic.taller1.taller1.users;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class AvatarService {
    public static final String DEFAULT_AVATAR = "https://avatar.io/default";

    public String cacheAvatar(@Nullable String avatar) {
        if(avatar  == null) {
            return DEFAULT_AVATAR;
        }
        return avatar;
    }
}

