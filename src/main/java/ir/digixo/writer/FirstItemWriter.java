package ir.digixo.writer;

import ir.digixo.model.StudentJdbc;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class FirstItemWriter implements ItemWriter<StudentJdbc> {

    @Override
    public void write(Chunk<? extends StudentJdbc> list) throws Exception {
        System.out.println("Inside item writer, Items : ");
        list.getItems().stream().forEach(System.out::println);
    }
}
