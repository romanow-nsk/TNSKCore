package romanow.abc.core.entity;

import lombok.Getter;
import lombok.Setter;
import romanow.abc.core.constants.Values;
import romanow.abc.core.entity.base.WorkSettingsBase;

public class WorkSettings extends WorkSettingsBase {
    @Getter @Setter private int careStoryHours=10;          // Интервал сохранения истории борта
}
