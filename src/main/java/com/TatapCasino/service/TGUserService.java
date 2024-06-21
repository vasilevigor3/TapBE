package com.TatapCasino.service;

import com.TatapCasino.converter.TGUserConverter;
import com.TatapCasino.dto.TGUserDTO;
import com.TatapCasino.model.TGUserModel;
import com.TatapCasino.repository.TGUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class TGUserService {

    @Autowired
    private TGUserRepository TGUserRepository;

    @Autowired
    private TGUserConverter tgUserConverter;

    @Value("${telegram.user.check.enabled}")
    private boolean isTelegramUserCheckEnabled;

    private static final String TELEGRAM_API_URL = "https://api.telegram.org/{token}/getChat?chat_id=";

    public List<TGUserDTO> getAllTGUsers() {
        final List<TGUserModel> tgUsersAll = TGUserRepository.findAll();
        final List<TGUserDTO> tgUserDTOS = tgUsersAll.stream().map(tgUserModel -> tgUserConverter.convertToDTO(tgUserModel)).toList();
        return tgUserDTOS;
    }

    @Transactional
    public TGUserModel saveTGUser(final Long id, final String userName) {
        final TGUserModel TGUser = new TGUserModel();
        TGUser.setId(id);
        TGUser.setName(userName);
        return TGUserRepository.save(TGUser);
    }

    @Transactional
    public List<TGUserModel> saveAllTGUsers(final List<TGUserModel> TGUsers) {
        return TGUserRepository.saveAll(TGUsers);
    }

    public Optional<TGUserModel> getTGUserById(final Long id) {
        return TGUserRepository.findById(id);
    }

    @Transactional
    public TGUserDTO getOrCreateTGUser(final Long id, final String userName) {
        final TGUserModel tgUserModel = getTGUserById(id).orElseGet(() -> saveTGUser(id, userName));
        if (isTelegramUserCheckEnabled) {
            if (isUserExistsInTelegram(id)) {

                return tgUserConverter.convertToDTO(tgUserModel);
            } else {
                throw new RuntimeException("User does not exist in Telegram.");
            }
        } else {
            return tgUserConverter.convertToDTO(tgUserModel);
        }
    }

    private boolean isUserExistsInTelegram(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = TELEGRAM_API_URL + id;

        try {
            String response = restTemplate.getForObject(url, String.class);
            return response.contains("\"ok\":true");
        } catch (Exception e) {
            return false;
        }
    }
}
