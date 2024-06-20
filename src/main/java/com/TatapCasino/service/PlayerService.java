package com.TatapCasino.service;

import com.TatapCasino.converter.PlayerConverter;
import com.TatapCasino.converter.TGUserToPlayerConverter;
import com.TatapCasino.dto.PlayerDTO;
import com.TatapCasino.exceptions.TGUserNotFoundException;
import com.TatapCasino.model.PlayerModel;
import com.TatapCasino.model.TGUserModel;
import com.TatapCasino.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TGUserService TGUserService;
    @Autowired
    private TGUserToPlayerConverter tgUserToPlayerConverter;
    @Autowired
    private PlayerConverter playerConverter;

    public List<PlayerModel> getAllPlayers() {
        return playerRepository.findAll();
    }

    public List<PlayerDTO> getAllPlayersDTO() {
        final List<PlayerModel> allPlayerModels = playerRepository.findAll();
        return allPlayerModels.stream()
                .map(playerModel -> playerConverter.convertToDTO(playerModel))
                .collect(Collectors.toList());
    }

    public PlayerModel savePlayer(PlayerModel player) {
        return playerRepository.save(player);
    }

    public List<PlayerModel> saveAllPlayers(List<PlayerModel> players) {
        return playerRepository.saveAll(players);
    }

    public List<PlayerModel> getPlayersByIds(List<Long> playersIds){
        return playerRepository.findAllById(playersIds);
    }

    public Optional<PlayerModel> getPlayerById(final long id){
        return playerRepository.findById(id);
    }
    public PlayerModel getOrCreatePlayer(final long id){
        Optional<PlayerModel> player = playerRepository.findById(id);
        if (player.isPresent()) {
            return player.get();
        }
        Optional<TGUserModel> tgUser = TGUserService.getTGUserById(id);

        final TGUserModel foundTGUser = tgUser
                .orElseThrow(() -> new TGUserNotFoundException("TGUserModel with id: " + id + " not found, please at first create TGUser with id: " + id));
        final PlayerModel convertedPlayer = tgUserToPlayerConverter.convert(foundTGUser);

        convertedPlayer.setTgUserAccount(foundTGUser);

        return savePlayer(convertedPlayer);
    }

    public PlayerDTO getOrCreatePlayerDTO(final long id){
        return playerConverter.convertToDTO(getOrCreatePlayer(id));
    }
}
