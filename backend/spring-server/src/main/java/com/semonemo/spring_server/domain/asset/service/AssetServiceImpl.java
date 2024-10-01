package com.semonemo.spring_server.domain.asset.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.semonemo.spring_server.domain.asset.dto.AssetDetailResponseDto;
import com.semonemo.spring_server.domain.asset.dto.AssetRequestDto;
import com.semonemo.spring_server.domain.asset.dto.AssetResponseDto;
import com.semonemo.spring_server.domain.asset.dto.AssetSellRequestDto;
import com.semonemo.spring_server.domain.asset.dto.AssetSellResponseDto;
import com.semonemo.spring_server.domain.asset.model.AssetImage;
import com.semonemo.spring_server.domain.asset.model.AssetLike;
import com.semonemo.spring_server.domain.asset.model.AssetSell;
import com.semonemo.spring_server.domain.asset.model.AssetTag;
import com.semonemo.spring_server.domain.asset.model.Atags;
import com.semonemo.spring_server.domain.asset.repository.assetimage.AssetImageRepository;
import com.semonemo.spring_server.domain.asset.repository.assetsell.AssetSellRepository;
import com.semonemo.spring_server.domain.asset.repository.assettag.AssetTagRepository;
import com.semonemo.spring_server.domain.asset.repository.atags.ATagsRepository;
import com.semonemo.spring_server.domain.asset.repository.like.AssetLikeRepository;
import com.semonemo.spring_server.domain.elasticsearch.service.ElasticsearchIndexChecker;
import com.semonemo.spring_server.domain.elasticsearch.service.ElasticsearchSyncService;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.repository.UserRepository;
import com.semonemo.spring_server.domain.user.service.UserService;
import com.semonemo.spring_server.global.common.CursorResult;

import co.elastic.clients.elasticsearch._types.SortOrder;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

	private final AssetImageRepository assetImageRepository;
	private final AssetSellRepository assetSellRepository;
	private final AssetLikeRepository assetLikeRepository;
	private final AssetTagRepository assetTagRepository;
	private final ATagsRepository aTagsRepository;
	private final UserRepository userRepository;
	private final ElasticsearchSyncService syncService;
	private final ElasticsearchIndexChecker indexChecker;

	// @PostConstruct
	// public void initializeElasticsearch() {
	// 	syncService.syncAllData();
	// }
	@Transactional
	@Override
	public void saveImage(AssetRequestDto assetRequestDto) {
		AssetImage assetImage = AssetImage.builder()
			.creator(assetRequestDto.getCreator())
			.imageUrl(assetRequestDto.getImageUrl())
			.build();
		assetImageRepository.save(assetImage);
	}

	@Transactional
	@Override
	public void registSale(Long nowid, AssetSellRequestDto assetSellRequestDto) {
		Long assetId = assetSellRequestDto.assetId();
		AssetImage assetImage = assetImageRepository.findById(assetId).
			orElse(null);
		AssetSell assetSell = AssetSell.builder()
			.assetId(assetId)
			.price(assetSellRequestDto.price())
			.build();
		System.out.println(assetSell.toString());
		assetSellRepository.save(assetSell);
		List<String> tagsname = assetSellRequestDto.tags();
		System.out.println(tagsname.toString());
		for (String tagname : tagsname) {
			if (aTagsRepository.existsByName(tagname)) {
				//이미 같은 태그가 존재하면? assettag에 걔로 추가
				System.out.println(tagname);
				Long atagId = aTagsRepository.findByName(tagname).getId();
				System.out.println(atagId);
				AssetTag assetTag = AssetTag.builder().
					assetSellId(assetSell.getId()).
					atagId(atagId).
					build();
				System.out.println(assetTag.toString());
				assetTagRepository.save(assetTag);
			} else {
				Atags atags = Atags.builder()
					.name(tagname)
					.build();
				aTagsRepository.save(atags);
				System.out.println(atags.toString());
				AssetTag assetTag = AssetTag.builder().
					assetSellId(assetSell.getId()).
					atagId(atags.getId()).
					build();
				System.out.println(assetTag.toString());
				assetTagRepository.save(assetTag);
			}
		}
		syncService.syncSellAsset(assetSell.getId());

	}

	@Transactional
	@Override
	public CursorResult<AssetSellResponseDto> getAllAsset(Long nowId, Long cursorId, int size) {
		List<AssetSell> assetSells;
		if (cursorId == null) {
			assetSells = assetSellRepository.findTopN( size + 1);
		} else {
			assetSells = assetSellRepository.findNextN( cursorId, size + 1);
		}
		List<AssetSellResponseDto> dtos = new ArrayList<>();
		boolean hasNext = false;
		if (assetSells.size() > size) {
			hasNext = true;
			assetSells = assetSells.subList(0, size);
		}

		for (AssetSell assetSell : assetSells) {
			AssetSellResponseDto dto = convertToDto(nowId, assetSell.getId());
			dtos.add(dto);
		}
		Long nextCursorId = hasNext ? assetSells.get(assetSells.size() - 1).getId() : null;
		return new CursorResult<>(dtos, nextCursorId, hasNext);
	}

	@Override
	public Page<AssetSellResponseDto> getAllAssetSort(Long nowId, String orderBy, int page, int size) {
		Pageable pageable =null ;
		String option = null;
		switch (orderBy) {
			case "latest":
				option = "createdAt";
				pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, option));
				break;
			case "oldest":
				option = "createdAt";
				pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, option));
				break;
			case "high":
				option = "price";
				pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, option));
				break;
			case "like":
				option = "likeCount";
				pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, option));
				break;
			case "hit":
				option = "hits";
				pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, option));
				break;
			case "sell":
				option = "purchaseCount";
				pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, option));
				break;
			case "low":
				option = "price";
				pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, option));
				break;

		}

		Page<AssetSell> assetSellPage = assetSellRepository.findAll(pageable);
		List<AssetSellResponseDto> dtos = assetSellPage.getContent().stream()
			.map(assetSell -> convertToDto(nowId, assetSell.getId()))
			.collect(Collectors.toList());

		return new PageImpl<>(dtos, pageable, assetSellPage.getTotalElements());
	}

	@Override
	public CursorResult<AssetResponseDto> getMyAsset(Long nowId, Long cursorId, int size) {
		List<AssetImage> assetImages;
		if (cursorId == null) {
			assetImages = assetImageRepository.findByUserIdTopN(nowId, size + 1);
		} else {
			assetImages = assetImageRepository.findByUserIdNextN(nowId, cursorId, size + 1);
		}
		List<AssetResponseDto> dtos = new ArrayList<>();
		boolean hasNext = false;
		if (assetImages.size() > size) {
			hasNext = true;
			assetImages = assetImages.subList(0, size);
		}

		for (AssetImage assetImage : assetImages) {
			AssetResponseDto dto = convertToAssetDto(nowId, assetImage.getId());
			dtos.add(dto);
		}
		Long nextCursorId = hasNext ? assetImages.get(assetImages.size() - 1).getId() : null;
		return new CursorResult<>(dtos, nextCursorId, hasNext);
	}

	@Override
	public CursorResult<AssetResponseDto> getUserAsset(Long nowid, Long userId, Long cursorId, int size) {
		List<AssetImage> assetImages;
		if (cursorId == null) {
			assetImages = assetImageRepository.findByCreatorTopN(nowid, userId, size + 1);
		} else {
			assetImages = assetImageRepository.findByCreatorIdNextN(nowid, userId, cursorId, size + 1);
		}
		List<AssetResponseDto> dtos = new ArrayList<>();
		boolean hasNext = false;
		if (assetImages.size() > size) {
			hasNext = true;
			assetImages = assetImages.subList(0, size);
		}
		for (AssetImage assetImage : assetImages) {
			AssetResponseDto dto = convertToAssetDto(nowid, assetImage.getId());
			dtos.add(dto);
		}
		Long nextCursorId = hasNext ? assetImages.get(assetImages.size() - 1).getId() : null;
		return new CursorResult<>(dtos, nextCursorId, hasNext);
	}

	@Transactional
	@Override
	public void like(Long nowid, Long assetSellId) {
		AssetLike like = AssetLike.builder()
			.assetSellId(assetSellId)
			.userId(nowid)
			.build();
		assetLikeRepository.save(like);
		assetSellRepository.updateCount(1, assetSellId);
		syncService.syncData(assetSellId, "like");

	}

	@Transactional
	@Override
	public void dislike(Long nowid, Long assetSellId) {
		AssetLike like = assetLikeRepository.findByUserIdAndAssetSellId(nowid, assetSellId);
		assetLikeRepository.delete(like);
		AssetSell assetSell = assetSellRepository.findById(assetSellId)
			.orElseThrow(() -> new RuntimeException("Asset Sell Not Found"));
		if (assetSell.getLikeCount() > -1) {
			assetSellRepository.updateCount(-1, assetSellId);
		}
		syncService.syncData(assetSellId, "dislike");
	}

	@Transactional
	@Override
	public boolean checkLike(Long nowid, Long assetSellId) {
		return assetLikeRepository.existsByUserIdAndAssetSellId(nowid, assetSellId);
	}

	@Override
	public AssetResponseDto getAssetDetail(Long nowid, Long assetId) {
		return convertToAssetDto(nowid, assetId);
	}

	@Transactional
	@Override
	public AssetDetailResponseDto getAssetSellDetail(Long nowid, Long assetSellId) {
		assetSellRepository.plusHits(assetSellId);
		syncService.syncData(assetSellId, "hits");
		return convertToDetailDto(nowid, assetSellId);
	}

	private AssetDetailResponseDto convertToDetailDto(Long nowid, Long assetSellId) {
		AssetSell assetSell = assetSellRepository.findById(assetSellId)
			.orElseThrow(() -> new IllegalArgumentException("Asset id not found"));

		AssetImage assetImage = assetImageRepository.findById(assetSell.getAssetId())
			.orElseThrow(() -> new IllegalArgumentException("Asset id not found"));

		boolean isLiked = assetLikeRepository.existsByUserIdAndAssetSellId(nowid, assetSellId);
		Users user = userRepository.findById(assetImage.getCreator())
			.orElseThrow(() -> new IllegalArgumentException("User id not found"));
		// List<Long> tagId= assetTagRepository.findAllByAssetSellId(assetSellId);
		// List<Atags> atags = new ArrayList<>();  // null이 아닌 빈 리스트로 초기화
		// for (Long id : tagId) {
		// 	aTagsRepository.findById(id).ifPresent(atags::add); // Optional을 사용하여 null 체크 후 추가
		// }
		List<String> tags = assetTagRepository.findTagsByAssetSellId(assetSellId);
		AssetDetailResponseDto dto = new AssetDetailResponseDto(
			assetSell.getAssetId(),
			assetSell.getId(),
			assetImage.getCreator(),
			assetImage.getImageUrl(),
			assetImage.getCreatedAt(),
			assetSell.getHits(),
			assetSell.getLikeCount(),
			user.getNickname(),
			assetSell.getPrice(),
			isLiked,
			tags

		);

		return dto;
	}

	private AssetResponseDto convertToAssetDto(Long nowid, Long assetId) {
		AssetImage assetImage = assetImageRepository.findById(assetId)
			.orElseThrow(() -> new IllegalArgumentException("Asset id not found"));
		boolean isLiked = assetLikeRepository.existsByUserIdAndAssetSellId(nowid, assetId);
		AssetResponseDto assetResponseDto = new AssetResponseDto(
			assetImage.getId(),
			assetImage.getCreator(),
			assetImage.getImageUrl(),
			isLiked
		);
		return assetResponseDto;
	}

	private AssetSellResponseDto convertToDto(Long nowid, Long assetSellId) {
		AssetSell assetSell = assetSellRepository.findById(assetSellId)
			.orElseThrow(() -> new IllegalArgumentException("Asset id not found"));

		AssetImage assetImage = assetImageRepository.findById(assetSell.getAssetId())
			.orElseThrow(() -> new IllegalArgumentException("Asset id not found"));

		boolean isLiked = assetLikeRepository.existsByUserIdAndAssetSellId(nowid, assetSellId);
		Users user = userRepository.findById(assetImage.getCreator())
			.orElseThrow(() -> new IllegalArgumentException("User id not found"));

		AssetSellResponseDto dto = new AssetSellResponseDto(
			assetSell.getAssetId(),
			assetSell.getId(),
			assetImage.getCreator(),
			assetImage.getImageUrl(),
			assetImage.getCreatedAt(),
			assetSell.getHits(),
			assetSell.getLikeCount(),
			user.getNickname(),
			assetSell.getPrice(),
			isLiked
		);
		return dto;
	}
}