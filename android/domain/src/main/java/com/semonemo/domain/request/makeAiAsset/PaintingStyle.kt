package com.semonemo.domain.request.makeAiAsset

sealed class PaintingStyle(
    open val title: String,
    open val prompt: String = "",
    open val negativePrompt: String = "",
    open val model: String = "",
) {
    sealed class Realistic(
        override val title: String = "실사",
        override val prompt: String,
        override val negativePrompt: String,
        override val model: String,
    ) : PaintingStyle(title, prompt, negativePrompt, model) {
        data object People : Realistic(
            prompt = "best quality, masterpiece, 8k, intricate, high detail, (extremely detailed face), (extremely detailed hands), (extremely detailed hair), ultra high res, (photorealistic:1.4), human",
            negativePrompt = "cartoon, painting, illustration, (worst quality, low quality, normal quality:2), ugly, BadDream, (UnrealisticDream:1.2), (nsfw:1.5), (naked:1.5), (nude:1.5), bad-artist, bad-artist-anime, bad_prompt_version2, badhandv4, EasyNegative, ng_deepnegative_v1_75t",
            model = "dreamshaper_8.safetensors [879db523c3]",
        )

        data object Animal : Realistic(
            prompt = "best quality, masterpiece, 8k, intricate, high detail, ultra high res, (photorealistic:1.4), animal",
            negativePrompt = "cartoon, painting, illustration, (worst quality, low quality, normal quality:2), BadDream, (UnrealisticDream:1.2), (nsfw:1.5), (naked:1.5), (nude:1.5), bad-artist, bad-artist-anime, bad_prompt_version2, badhandv4, EasyNegative, ng_deepnegative_v1_75t",
            model = "dreamshaper_8.safetensors [879db523c3]",
        )

        data object Ect : Realistic(
            prompt = "best quality, masterpiece, 8k, intricate, high detail, (extremely detailed face), (extremely detailed hands), (extremely detailed hair), ultra high res, (photorealistic:1.4), not human, not animal",
            negativePrompt = "cartoon, painting, illustration, (worst quality, low quality, normal quality:2), ugly, BadDream, (UnrealisticDream:1.2), (nsfw:1.5), (naked:1.5), (nude:1.5), bad-artist, bad-artist-anime, bad_prompt_version2, badhandv4, EasyNegative, ng_deepnegative_v1_75t",
            model = "dreamshaper_8.safetensors [879db523c3]",
        )
    }

    sealed class Anime(
        override val title: String = "애니메이션",
        override val prompt: String,
        override val negativePrompt: String,
        override val model: String = "cetusMix_v4.safetensors [b42b09ff12]",
    ) : PaintingStyle(
            title,
            prompt,
            negativePrompt,
            model,
        ) {
        data object People : Anime(
            prompt = "est quality, masterpiece , ultra high res, human",
            negativePrompt =
                "(worst quality, low quality, normal quality:2), ugly, BadDream, (UnrealisticDream:1.2), (nsfw:1.5), (naked:1.5), (nude:1.5), bad-artist, bad-artist-anime, bad_prompt_version2, badhandv4, EasyNegative, ng_deepnegative_v1_75t",
        )

        data object Animal : Anime(
            prompt = "best quality, masterpiece , ultra high res, animal",
            negativePrompt = "(worst quality, low quality, normal quality:2), ugly, BadDream, (UnrealisticDream:1.2), (nsfw:1.5), (naked:1.5), (nude:1.5), bad-artist, bad-artist-anime, bad_prompt_version2, badhandv4, EasyNegative, ng_deepnegative_v1_75t",
        )

        data object Ect : Anime(
            prompt = "est quality, masterpiece , ultra high res, not human, not animal",
            negativePrompt =
                "(worst quality, low quality, normal quality:2), ugly, BadDream, (UnrealisticDream:1.2), (nsfw:1.5), (naked:1.5), (nude:1.5), bad-artist, bad-artist-anime, bad_prompt_version2, badhandv4, EasyNegative, ng_deepnegative_v1_75t",
        )
    }

    sealed class Cartoon(
        override val title: String = "카툰",
        override val prompt: String,
        override val negativePrompt: String,
        override val model: String = "toonyou_beta6.safetensors [e8d456c42e]",
    ) : PaintingStyle(
            title,
            prompt,
            negativePrompt,
            model,
        ) {
        data object People : Cartoon(
            prompt = "best quality, masterpiece , ultra high res, human",
            negativePrompt = "(worst quality, low quality, normal quality:2), ugly, BadDream, (UnrealisticDream:1.2), (nsfw:1.5), (naked:1.5), (nude:1.5), bad-artist, bad-artist-anime, bad_prompt_version2, badhandv4, EasyNegative, ng_deepnegative_v1_75t",
        )

        data object Animal : Cartoon(
            prompt = "best quality, masterpiece , ultra high res, animal",
            negativePrompt = "(worst quality, low quality, normal quality:2), ugly, BadDream, (UnrealisticDream:1.2), (nsfw:1.5), (naked:1.5), (nude:1.5), bad-artist, bad-artist-anime, bad_prompt_version2, badhandv4, EasyNegative, ng_deepnegative_v1_75t",
        )

        data object Ect : Cartoon(
            prompt = "est quality, masterpiece , ultra high res, not human, not animal",
            negativePrompt =
                "(worst quality, low quality, normal quality:2), ugly, BadDream, (UnrealisticDream:1.2), (nsfw:1.5), (naked:1.5), (nude:1.5), bad-artist, bad-artist-anime, bad_prompt_version2, badhandv4, EasyNegative, ng_deepnegative_v1_75t",
        )
    }

    data class None(
        override val title: String = "없음",
    ) : PaintingStyle(title)
}
