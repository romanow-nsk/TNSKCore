package romanow.abc.core.entity.subjectarea;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.entity.Entity;

public class TSMSBank extends Entity {
    @Getter @Setter private int smsPhoneNumber=900;
    @Getter @Setter private String smsSendTemplate="ПЕРЕВОД *1 *2";                                 // *1 - номер телефона, *2 -  сумма
    @Getter @Setter private String smsAskTemplate="Подтвердите Ваш платеж! Код подтверждения *3";   // *3 - код подтверждения
    @Getter @Setter private String smsAskSendTemplate="*3";                                         // *3 - код подтверждения
    @Getter @Setter private String smsRecTemplate="Ваш платеж принят";                              // *3 - код подтверждения
}
