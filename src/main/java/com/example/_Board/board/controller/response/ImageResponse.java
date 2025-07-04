package com.example._Board.board.controller.response;

import com.example._Board.board.domain.Board;
import com.example._Board.board.domain.Image;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ImageResponse {

    private String fileUrl;

    public static List<ImageResponse> toResponse(Board board) {
        List<ImageResponse> list = new ArrayList<>();
        for (Image image : board.getImages()) {
            ImageResponse imageResponse = ImageResponse.builder()
                    .fileUrl(image.getFileUrl())
                    .build();
            list.add(imageResponse);
        }
        return list;
    }


    @QueryProjection
    public ImageResponse(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
