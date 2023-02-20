package shop.mtcoding.blog.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoveRepository<T> {

        public Love findByBoardIdAndUserId(@Param("boardId") int boardId, @Param("userId") int userId);

        public List<T> findAll();

        public Love findById(int id);

        public int insert(Love love);

        public int updateById(Love love);

        public int deleteById(int id);
}