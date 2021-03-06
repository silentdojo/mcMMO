package com.gmail.nossr50.skills.acrobatics;

import org.bukkit.event.entity.EntityDamageEvent;

import com.gmail.nossr50.datatypes.PlayerProfile;
import com.gmail.nossr50.locale.LocaleLoader;
import com.gmail.nossr50.skills.SkillType;
import com.gmail.nossr50.skills.SkillTools;
import com.gmail.nossr50.util.Misc;

public class DodgeEventHandler extends AcrobaticsEventHandler {
    protected DodgeEventHandler(AcrobaticsManager manager, EntityDamageEvent event) {
        super(manager, event);

        calculateSkillModifier();
        calculateModifiedDamage();
    }

    @Override
    protected void calculateSkillModifier() {
        this.skillModifier = Misc.skillCheck(manager.getSkillLevel(), Acrobatics.dodgeMaxBonusLevel);
    }

    @Override
    protected void calculateModifiedDamage() {
        int modifiedDamage = damage / 2;

        if (modifiedDamage <= 0) {
            modifiedDamage = 1;
        }

        this.modifiedDamage = modifiedDamage;
    }

    @Override
    protected void modifyEventDamage() {
        event.setDamage(modifiedDamage);
    }

    @Override
    protected void sendAbilityMessage() {
        player.sendMessage(LocaleLoader.getString("Acrobatics.Combat.Proc"));
    }

    @Override
    protected void processXPGain(int xp) {
        PlayerProfile profile = manager.getProfile();

        if (System.currentTimeMillis() >= profile.getRespawnATS() + Misc.PLAYER_RESPAWN_COOLDOWN_SECONDS) {
            SkillTools.xpProcessing(player, profile, SkillType.ACROBATICS, xp);
        }
    }
}
