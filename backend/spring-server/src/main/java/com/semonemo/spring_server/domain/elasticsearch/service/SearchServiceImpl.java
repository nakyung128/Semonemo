package com.semonemo.spring_server.domain.elasticsearch.service;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.domain.elasticsearch.repository.AssetElasticsearchRepository;
import com.semonemo.spring_server.global.common.CursorResult;

@Service
public class SearchServiceImpl implements SearchService {
	private AssetElasticsearchRepository assetElasticsearchRepository;
	private final ElasticsearchOperations elasticsearchOperations;
	private ElasticsearchSyncService syncService;

	public SearchServiceImpl(AssetElasticsearchRepository assetElasticsearchRepository, ElasticsearchOperations elasticsearchOperations, ElasticsearchSyncService syncService) {
		this.assetElasticsearchRepository = assetElasticsearchRepository;
        this.elasticsearchOperations = elasticsearchOperations;
		this.syncService = syncService;
	}


	// @PostConstruct
	// public void initializeElasticsearch() {
	// 	syncService.syncAllData();
	// }
	@Override
	public CursorResult<AssetSellDocument> searchAsset(Long nowid, String keyword, Long cursorId, int size) {
		return assetElasticsearchRepository.findByTagKeyword(keyword, cursorId, size);
	}

	@Override
	public AssetSellDocument getAsset(Long id) {
		AssetSellDocument assetImageDocument = assetElasticsearchRepository.findById(id)
			.orElse(null);
		return assetImageDocument;
	}

	//	@Override
//	public Page<AssetImageDocument> searchAsset(String keyword, int page, int size) {
//		Pageable pageable = PageRequest.of(page, size);
//		return assetElasticsearchRepository.findByTag(keyword, pageable);
//	}
// @Override
// public Page<AssetImageDocument> searchAsset(String keyword, int page, int size) {
// 	Pageable pageable = PageRequest.of(page, size);
//
// 	if (keyword != null && !keyword.isEmpty()) {
// 		return assetElasticsearchRepository.findByTag(keyword, pageable);
// 	} else {
// 		Query query = new CriteriaQuery(new Criteria());
// 		query.setPageable(pageable);
// 		SearchHits<AssetImageDocument> searchHits = elasticsearchOperations.search(query, AssetImageDocument.class);
// 		return assetElasticsearchRepository.findAll(pageable);
// 	}
// }
}
