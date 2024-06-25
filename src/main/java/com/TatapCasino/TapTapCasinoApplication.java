package com.TatapCasino;

import com.TatapCasino.dto.RoomDTO;
import com.TatapCasino.model.PlayerModel;
import com.TatapCasino.model.TGUserModel;
import com.TatapCasino.repository.PlayerRepository;
import com.TatapCasino.repository.RoomRepository;
import com.TatapCasino.service.GameService;
import com.TatapCasino.service.PlayerService;
import com.TatapCasino.service.RoomService;
import com.TatapCasino.service.TGUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class TapTapCasinoApplication implements CommandLineRunner {

	@Autowired
	private PlayerService playerService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private TGUserService tgUserService;

	@Autowired
	private GameService gameService;

	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private PlayerRepository playerRepository;

	public static void main(String[] args) {
		SpringApplication.run(TapTapCasinoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		final TGUserModel testUser1 = tgUserService.saveTGUser(1L, "testUser1");
		final TGUserModel testUser2 = tgUserService.saveTGUser(2L, "testUser2");
		final TGUserModel testUser3 = tgUserService.saveTGUser(3L, "testUser3");

		final PlayerModel testPlayer1 = playerService.getOrCreatePlayer(testUser1.getId());
		final PlayerModel testPlayer2 = playerService.getOrCreatePlayer(testUser2.getId());
		final PlayerModel testPlayer3 = playerService.getOrCreatePlayer(testUser3.getId());

		roomService.createRoom(createDummyRoomDTO(1L, List.of(1L)));
		roomService.joinPlayerToRoom(createDummyRoomDTO(1L, List.of(2L)));

		roomService.createRoom(createDummyRoomDTO(2L, List.of(3L)));
//		roomService.joinPlayerToRoom(createDummyRoomDTO(1L, List.of(2L)));

	}

	public RoomDTO createDummyRoomDTO(final Long id, final List<Long> ids){
		final RoomDTO roomDTO = new RoomDTO();
		roomDTO.setRoomId(id);
		roomDTO.setRoomName("Dummy Room Name");
		roomDTO.setMaxPlayers(3);
		roomDTO.setOwnerId(ids.get(0));
		roomDTO.setGameType("ROULETTE");
		roomDTO.setPlayerIds(ids);
		roomDTO.setBet(0.1);
		return roomDTO;
	}
}
