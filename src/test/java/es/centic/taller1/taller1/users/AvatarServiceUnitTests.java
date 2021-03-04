package es.centic.taller1.taller1.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class AvatarServiceUnitTests {
    
    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeaderUriMock;
    @Mock
    private WebClient.RequestBodyUriSpec requestUriMock;
    @Mock
    private WebClient.RequestBodySpec requestBodyMock;
    @Mock
    private WebClient.ResponseSpec responseMock;
    @Mock
    private WebClient.RequestHeadersSpec requestHeaderMock;

    private AvatarService avatarService;

    @BeforeEach
    void setupTests() {
        avatarService = new AvatarService(webClientMock);
    }

    // HU: Usar un api externa para tener imágenes aleatorias para los avatares de los usuarios

    @Test
    void getImageFromAPIWhenAvatarIsNull() {
        ApiImage mock = new ApiImage();
        mock.setImage("http:/avatar.io/myavatar");

        given(webClientMock.get()).willReturn(requestHeaderUriMock);
        given(requestHeaderUriMock.uri("/avatars")).willReturn(requestHeaderMock);
        given(requestHeaderMock.retrieve()).willReturn(responseMock);
        given(responseMock.bodyToMono(ApiImage.class)).willReturn(Mono.just(mock));

        assertThat(avatarService.cacheAvatar(null)).isEqualTo(mock.getImage());
    }
}
