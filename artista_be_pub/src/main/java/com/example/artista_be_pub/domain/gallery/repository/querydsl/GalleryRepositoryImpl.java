package com.artista.main.domain.gallery.repository.querydsl;

import com.artista.main.domain.gallery.dto.ArtListDto;
import com.artista.main.domain.gallery.dto.GalleryInfoDto;
import com.artista.main.domain.gallery.dto.SerchListDto;
import com.artista.main.domain.user.entity.UserEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.*;

import static com.artista.main.domain.gallery.entity.QArtImgEntity.artImgEntity;
import static com.artista.main.domain.gallery.entity.QGalleryEntity.galleryEntity;
import static com.artista.main.domain.gallery.entity.QLikeEntity.likeEntity;
import static com.artista.main.domain.user.entity.QUserEntity.userEntity;
import static com.artista.main.domain.category.entity.QCategoryEntity.categoryEntity;
import static com.artista.main.domain.gallery.entity.QRecommandEntity.recommandEntity;

@RequiredArgsConstructor
@Slf4j
public class GalleryRepositoryImpl implements GalleryRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    /**
     * 인기 갤러리 조회
     * @param size
     * @return
     */
    @Override
    public Optional<List<GalleryInfoDto>> popularGalleryInfo(long size){

        JPAQuery<GalleryInfoDto> subquery = new JPAQuery<>(entityManager);
        List<Long> topLikedGalleryIds = subquery
                .select(likeEntity.galleryEntity.id.as("id"))
                .from(likeEntity)
                .groupBy(likeEntity.galleryEntity.id)
                .orderBy(likeEntity.galleryEntity.id.count().desc(), likeEntity.galleryEntity.id.desc())
                .limit(size)
                .fetch();

        List<Long> sortedTopLikedGalleryIds = new ArrayList<>(topLikedGalleryIds);

        //스칼라 서브쿼리에 limit이 적용되지 않아서 2중 서브쿼리로 해결
        List<GalleryInfoDto> result = (List<GalleryInfoDto>) queryFactory
                .select(Projections.constructor(GalleryInfoDto.class,
                                galleryEntity.id,
                                galleryEntity.artTitle,
                                galleryEntity.userEntity.userId,
                                ExpressionUtils.as(
                                        JPAExpressions.select(artImgEntity.filePath.concat(artImgEntity.fileName))
                                                .from(artImgEntity)
                                                .where(artImgEntity.id.eq(
                                                        JPAExpressions.select(artImgEntity.id)
                                                                .from(artImgEntity)
                                                                .where(artImgEntity.galleryEntity.eq(galleryEntity),
                                                                        artImgEntity.orderNo.eq(1))
                                                )),
                                "artUrl")
                ))
                .from(galleryEntity)
                .where(galleryEntity.id.in(
                        topLikedGalleryIds
                ))
                .fetch();
//        topLikedGalleryIds.stream().forEach(System.out::println);
        result.sort(Comparator.comparingLong(e -> topLikedGalleryIds.indexOf(e.getArtId())));
        return Optional.ofNullable(result);
    }

    /**
     * 좋아요 한 작품 목록 조회
     * @param userEntity
     * @param pageable
     * @return
     */
    @Override
    public Optional<List<GalleryInfoDto>> getLikeArtList(UserEntity userEntity, Pageable pageable){

        List<GalleryInfoDto> result = (List<GalleryInfoDto>) queryFactory
                .select(Projections.constructor(GalleryInfoDto.class,
                        galleryEntity.id,
                        galleryEntity.artTitle,
                        galleryEntity.userEntity.userId,
                        ExpressionUtils.as(
                                JPAExpressions.select(artImgEntity.filePath.concat(artImgEntity.fileName))
                                        .from(artImgEntity)
                                        .where(artImgEntity.id.eq(
                                                JPAExpressions.select(artImgEntity.id)
                                                        .from(artImgEntity)
                                                        .where(artImgEntity.galleryEntity.eq(galleryEntity),
                                                                artImgEntity.orderNo.eq(1))
                                        )),
                                "artUrl")
                ))
                .from(likeEntity)
                .innerJoin(galleryEntity).on(likeEntity.galleryEntity.eq(galleryEntity))
                .where(likeEntity.userEntity.eq(userEntity))
                .orderBy(likeEntity.id.desc())
                .offset(pageable.getOffset())   //페이지 번호
                .limit(pageable.getPageSize())  //페이지 사이즈
                .fetch();

        return Optional.ofNullable(result);
    }

    /**
     * 검색
     * @param serchType
     * @param serchValue
     * @param pageable
     * @return
     */
    @Override
    public Optional<List<SerchListDto>> serch(String serchType, String serchValue, Pageable pageable){
        BooleanBuilder builder = new BooleanBuilder();
        if(serchType.equals("S") || serchType.equals("A")){
            builder.or(galleryEntity.artTitle.contains(serchValue));
        }
        if(serchType.equals("S") || serchType.equals("AT")){
            builder.or(userEntity.nickName.contains(serchValue.toUpperCase()));
        }
        if(serchType.equals("S") || serchType.equals("C")){
            builder.or(categoryEntity.categoryName.contains(serchValue));
        }

        List<SerchListDto> result = (List<SerchListDto>) queryFactory
                .select(Projections.constructor(SerchListDto.class,
                        galleryEntity.id,
                        galleryEntity.artTitle,
                        galleryEntity.userEntity.nickName,
                        ExpressionUtils.as(
                                JPAExpressions.select(artImgEntity.filePath.concat(artImgEntity.fileName))
                                        .from(artImgEntity)
                                        .where(artImgEntity.id.eq(
                                                JPAExpressions.select(artImgEntity.id)
                                                        .from(artImgEntity)
                                                        .where(artImgEntity.galleryEntity.eq(galleryEntity),
                                                                artImgEntity.orderNo.eq(1))
                                        )),
                                "artUrl")
                        ))

                .from(galleryEntity)
                .innerJoin(categoryEntity).on(galleryEntity.categoryEntity.eq(categoryEntity))
                .innerJoin(userEntity).on(galleryEntity.userEntity.eq(userEntity))
                .where(builder)
                .orderBy(galleryEntity.id.desc())
                .offset(pageable.getOffset())   //페이지 번호
                .limit(pageable.getPageSize())  //페이지 사이즈
                .fetch();

        return Optional.ofNullable(result);
    }

    /**
     * 갤러리 목록 조회(페이징)
     * categoryType (전체:A, 서양화:W, 동양화:E)
     * orderType (추천순:R, 인기순:P, 날짜순:D)
     * @param orderType
     * @param pageable
     * @return
     */
    @Override
        public Optional<List<ArtListDto>> getGalleryList(Long categoryId, String orderType, Pageable pageable){

            List<ArtListDto> result = (List<ArtListDto>) queryFactory
                    .select(Projections.constructor(ArtListDto.class,
                            galleryEntity.id,
                            galleryEntity.artTitle,
                            galleryEntity.userEntity.nickName,
                            ExpressionUtils.as(
                                    JPAExpressions.select(artImgEntity.filePath.concat(artImgEntity.fileName))
                                            .from(artImgEntity)
                                            .where(artImgEntity.id.eq(
                                                    JPAExpressions.select(artImgEntity.id)
                                                            .from(artImgEntity)
                                                            .where(artImgEntity.galleryEntity.eq(galleryEntity),
                                                                    artImgEntity.orderNo.eq(1))
                                            )),
                                    "artUrl"),
                            ExpressionUtils.as(
                                    JPAExpressions.select(likeEntity.count())
                                            .from(likeEntity)
                                            .where(likeEntity.galleryEntity.eq(galleryEntity))
                                            .groupBy(galleryEntity),
                                    "likeCnt")
                            ))
                    .from(galleryEntity)
                    .innerJoin(categoryEntity).on(galleryEntity.categoryEntity.eq(categoryEntity))
                    .leftJoin(recommandEntity).on(galleryEntity.recommandEntity.eq(recommandEntity))
                    .where(category(categoryId))
                    .orderBy(sortByField(orderType), galleryEntity.id.desc())
                    .offset(pageable.getOffset())   //페이지 번호
                    .limit(pageable.getPageSize())  //페이지 사이즈
                    .fetch();

        return Optional.ofNullable(result);
    }

    /**
     * 작품명 조회
     * @param categoryId
     * @return
             */
    private BooleanExpression category(Long categoryId) {
        if(categoryId > 0){
            return categoryEntity.id.eq(categoryId);
        }

        return null;
    }

    /**
     * 동적 정렬 처리(추천순:R, 인기순:P, 날짜순:D)
     * @param orderType
     * @return
     */
    private OrderSpecifier<?> sortByField(final String orderType) {
        StringPath aliasQuantity = Expressions.stringPath("likeCnt");

        Order order = Order.DESC;

        if (orderType.equals("P")) {
            return new OrderSpecifier<>(order, aliasQuantity);
        }
        else if (orderType.equals("D")) {
            return new OrderSpecifier<>(order, galleryEntity.rgsttDtm);
        }
        else {
            return new OrderSpecifier<>(order, recommandEntity.id);
        }
    }


}
