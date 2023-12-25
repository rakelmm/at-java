package com.rakel.atjava.model;

import com.google.gson.annotations.SerializedName;

public record JsonPlaceHolderDadosRecebidos(
        @SerializedName("userId") Long idUsuario,
        Long id,
        @SerializedName("title") String titulo,
        @SerializedName("body") String descricao
) {
}
