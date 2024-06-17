package cn.attackme.muleproject.repository;

import cn.attackme.muleproject.entity.CanvasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface CanvasRepository extends JpaRepository<CanvasEntity, Long> {

    List<CanvasEntity> findByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE CanvasEntity c SET c.canvasName = :canvasName WHERE c.id = :id")
    void updateNameById(Long id, String canvasName);

    @Transactional
    @Modifying
    @Query("UPDATE CanvasEntity c SET c.canvasJson = :canvas WHERE c.id = :id")
    void updateCanvasJsonById(Long id, String canvas);

    void deleteById(Long id);

    @Query("SELECT c.canvasName FROM CanvasEntity c WHERE c.id = :id")
    String findCanvasNameById(Long id);

    boolean existsByCanvasNameAndUserId(String newName, Long userId);

    CanvasEntity findByCanvasNameAndUserId(String name, Long userId);
}
