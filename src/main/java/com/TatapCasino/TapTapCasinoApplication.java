package com.TatapCasino;

import com.TatapCasino.model.PlayerModel;
import com.TatapCasino.model.TGUserModel;
import com.TatapCasino.model.RoomModel;
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

	public static void main(String[] args) {
		SpringApplication.run(TapTapCasinoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		PlayerModel testPlayer = new PlayerModel();
		testPlayer.setId(1L);
		testPlayer.setName("TestUser");
		PlayerModel testPlayer2 = new PlayerModel();
		testPlayer2.setId(2L);
		testPlayer2.setName("TestUser2");
		playerService.saveAllPlayers(List.of(testPlayer,testPlayer2));

		tgUserService.saveTGUser(1L, "ololo");



		RoomModel testRoom = new RoomModel();
		testRoom.setRoomName("Play roulette");
		testRoom.setPlayers(List.of(testPlayer,testPlayer2));
		testRoom.setMaxPlayers(3);
		testRoom.setBet(2.0);
		roomService.saveRoom(testRoom);
	}
}
