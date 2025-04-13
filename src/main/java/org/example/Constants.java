package org.example;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import java.util.Base64;

public final class Constants
{
    public static final class Numbers {
        public static final int ZERO = 0;
        public static final int ONE = 1;
        public static final int TEN = 10;

        private Numbers() {}
    }

    public static ItemStack fromBase64(String base64)
    {
        try
        {
            String yaml = new String(Base64.getDecoder().decode(base64));

            YamlConfiguration config = new YamlConfiguration();
            config.loadFromString(yaml);
            return config.getItemStack("item");
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static final String CLEAN_TAGS_POTION = "aXRlbToKICA9PTogb3JnLmJ1a2tpdC5pbnZlbnRvcnkuSXRlbVN0YWNrCiAgdjogMzEwNQogIHR5cGU6IFBPVElPTgogIG1ldGE6CiAgICA9PTogSXRlbU1ldGEKICAgIG1ldGEtdHlwZTogUE9USU9OCiAgICBJdGVtRmxhZ3M6CiAgICAtIEhJREVfUE9USU9OX0VGRkVDVFMKICAgIGN1c3RvbS1jb2xvcjoKICAgICAgPT06IENvbG9yCiAgICAgIFJFRDogMjU1CiAgICAgIEJMVUU6IDI1NQogICAgICBHUkVFTjogMjU1CiAgICBjdXN0b20tZWZmZWN0czoKICAgIC0gPT06IFBvdGlvbkVmZmVjdAogICAgICBlZmZlY3Q6IDEKICAgICAgZHVyYXRpb246IDAKICAgICAgYW1wbGlmaWVyOiAwCiAgICAgIGFtYmllbnQ6IGZhbHNlCiAgICAgIGhhcy1wYXJ0aWNsZXM6IHRydWUKICAgICAgaGFzLWljb246IHRydWUK";

    private Constants() {}
}