package br.com.sw2you.realmeet.utils;

import static br.com.sw2you.realmeet.domain.entity.Room.Builder;
import static br.com.sw2you.realmeet.domain.entity.Room.newBuilder;
import static br.com.sw2you.realmeet.utils.TestConstants.DEFAULT_ROOM_NAME;
import static br.com.sw2you.realmeet.utils.TestConstants.DEFAULT_ROOM_SEATS;

public final class TestDataCreator {

    private TestDataCreator() {}

    public static Builder newRoomBuilder() {
        return newBuilder().name(DEFAULT_ROOM_NAME).seats(DEFAULT_ROOM_SEATS);
    }
}