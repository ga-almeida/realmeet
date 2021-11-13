package br.com.sw2you.realmeet.controller;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import br.com.sw2you.realmeet.api.facade.RoomsApi;
import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.api.model.RoomDTO;
import br.com.sw2you.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2you.realmeet.service.RoomService;
import br.com.sw2you.realmeet.util.ResponseEntityUtils;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController implements RoomsApi {
    private final Executor controllersExecutor;
    private final RoomService roomService;

    public RoomController(Executor controllersExecutor, RoomService roomService) {
        this.controllersExecutor = controllersExecutor;
        this.roomService = roomService;
    }

    @Override
    public CompletableFuture<ResponseEntity<RoomDTO>> find(Long id) {
        return supplyAsync(() -> roomService.find(id), controllersExecutor).thenApply(ResponseEntityUtils::ok);
    }

    @Override
    public CompletableFuture<ResponseEntity<Void>> delete(Long id) {
        return runAsync(() -> roomService.delete(id), controllersExecutor).thenApply(ResponseEntityUtils::noContent);
    }

    @Override
    public CompletableFuture<ResponseEntity<RoomDTO>> create(CreateRoomDTO createRoomDTO) {
        return supplyAsync(() -> roomService.create(createRoomDTO), controllersExecutor)
            .thenApply(ResponseEntityUtils::created);
    }

    @Override
    public CompletableFuture<ResponseEntity<Void>> update(Long id, UpdateRoomDTO updateRoomDTO) {
        return runAsync(() -> roomService.update(id, updateRoomDTO), controllersExecutor)
            .thenApply(ResponseEntityUtils::noContent);
    }
}
