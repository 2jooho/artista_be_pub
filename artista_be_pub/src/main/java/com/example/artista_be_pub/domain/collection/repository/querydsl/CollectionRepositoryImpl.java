//package com.artista.main.domain.collection.repository.querydsl;
//
//import com.artista.main.domain.collection.dto.CollectionInfoDto;
//import com.querydsl.core.types.Projections;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.RequiredArgsConstructor;
//
//import javax.persistence.EntityManager;
//import java.util.Optional;
//
//import static com.artista.main.domain.collection.entity.QCollectionEntity.collectionEntity;
//import static com.artista.main.domain.user.entity.QUserEntity.userEntity;
//
//@RequiredArgsConstructor
//public class CollectionRepositoryImpl implements CollectionRepositoryCustom {
//    private final JPAQueryFactory queryFactory;
//    private final EntityManager entityManager;
//
//    @Override
//    public Optional<CollectionInfoDto> getCollectionInfo(long size){
////        JPAQuery<GalleryInfoDto> subquery = new JPAQuery<>(entityManager);
////        List<Long> topLikedGalleryIds = subquery
////                .select(likeEntity.galleryEntity.id)
////                .from(likeEntity)
////                .groupBy(likeEntity.galleryEntity.id)
////                .orderBy(likeEntity.galleryEntity.id.count().desc())
////                .limit(size)
////                .fetch();
//
//        CollectionInfoDto result = queryFactory
//                .select(Projections.constructor(CollectionInfoDto.class,
//
//                        ))
//                .from(userEntity)
//                .innerJoin(userEntity).on()
//                .where()
//                .fetchOne();
//
//        return Optional.ofNullable(result);
//    }
//}
