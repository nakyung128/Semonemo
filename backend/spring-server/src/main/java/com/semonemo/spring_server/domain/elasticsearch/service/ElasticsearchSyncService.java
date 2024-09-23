package com.semonemo.spring_server.domain.elasticsearch.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semonemo.spring_server.domain.asset.model.AssetImage;
import com.semonemo.spring_server.domain.asset.model.AssetSell;
import com.semonemo.spring_server.domain.asset.model.AssetTag;
import com.semonemo.spring_server.domain.asset.model.Atags;
import com.semonemo.spring_server.domain.asset.repository.assetimage.AssetImageRepository;
import com.semonemo.spring_server.domain.asset.repository.assetsell.AssetSellRepository;
import com.semonemo.spring_server.domain.asset.repository.assettag.AssetTagRepository;
import com.semonemo.spring_server.domain.asset.repository.atags.ATagsRepository;
import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.domain.elasticsearch.repository.AssetElasticsearchRepository;

@Service
public class ElasticsearchSyncService {

    @Autowired
    private AssetImageRepository assetImageRepository;

    @Autowired
    private AssetSellRepository assetSellRepository;

    @Autowired
    private AssetTagRepository assetTagRepository;

    @Autowired
    private ATagsRepository atagsRepository;

    @Autowired
    private AssetElasticsearchRepository assetElasticsearchRepository;

    @Autowired
    private ElasticsearchIndexChecker indexChecker;
    public void syncAllData() {
        List<AssetImage> allAssetImages = assetImageRepository.findAll();
        List<AssetSellDocument> documents = allAssetImages.stream()
            .map(assetImage -> {
                AssetSell assetSell= assetSellRepository.findByAssetId(assetImage.getAssetId());
                List<AssetTag> assetTags = assetTagRepository.findByAssetSellId(assetSell.getAssetSellId());
                List<Atags> tags = assetTags.stream()
                    .map(tag -> atagsRepository.findById(tag.getAtagId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
                return convertToDocument(assetImage, assetSell, tags);
            })
            .filter(obj -> true)
            .collect(Collectors.toList());
        assetElasticsearchRepository.saveAll(documents);
    }

    public void syncSingleAsset(Long assetId) {
        AssetImage assetImage = assetImageRepository.findById(assetId)
            .orElseThrow(() -> new RuntimeException("Asset not found"));
        AssetSell assetSell = assetSellRepository.findByAssetId(assetId);
        List<AssetTag> assetTags = assetTagRepository.findByAssetSellId(assetSell.getAssetSellId());
        List<Atags> tags = assetTags.stream()
            .map(tag -> atagsRepository.findById(tag.getAtagId()).orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        AssetSellDocument document = convertToDocument(assetImage, assetSell, tags);
        assetElasticsearchRepository.save(document);
    }
    private AssetSellDocument convertToDocument(AssetImage assetImage, AssetSell assetSell, List<Atags> tags) {
        AssetSellDocument document = new AssetSellDocument();

        document.setAssetSellId(assetSell.getAssetSellId());
        document.setCreator(assetImage.getCreator());
        document.setImageUrls(assetImage.getImageUrl());
        document.setAssetId(assetImage.getAssetId());
        document.setPrice(assetSell.getPrice());
        document.setHits(assetSell.getHits());
        document.setCreatedAt(assetSell.getCreatedAt());
        document.setLikeCount(assetSell.getLikeCount());

        List<AssetSellDocument.Tag> documentTags = tags.stream()
            .map(tag -> {
                AssetSellDocument.Tag documentTag = new AssetSellDocument.Tag();
                documentTag.setAtagId(tag.getAtagId());
                documentTag.setName(tag.getName());
                return documentTag;
            })
            .collect(Collectors.toList());
        document.setTags(documentTags);

        return document;
    }
}