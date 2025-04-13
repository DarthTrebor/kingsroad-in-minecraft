package org.example;

import org.bukkit.entity.Player;
import org.example.Potions.ActiveEffect;
import java.util.List;

public record ConnectedPlayer(Player player, List<ActiveEffect> effects)
{
    public void addEffect(ActiveEffect effect)
    {
        effects.add(effect);
    }
}
