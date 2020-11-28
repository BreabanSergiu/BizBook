package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.memory.InMemoryRepository;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


///Aceasta clasa implementeaza sablonul de proiectare Template Method; puteti inlucui solutia propusa cu un Factori (vezi mai jos)
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;
    protected  Repository<Long,User>repository;

    /**
     *
     * @param fileName String , representing the file name where data is loaded and written
     * @param validator Validator<T></>, validates the T object
     */
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName=fileName;
        loadData();

    }

    /**
     *
     *@param fileName String , representing the file name where data is loaded and written
      * @param validator Validator<T></>, validates the T object
     * @param repository, Repositori<T></>
     */
    public AbstractFileRepository(String fileName, Validator<E> validator, Repository<Long, User> repository) {
        super(validator);
        this.fileName=fileName;
        this.repository = repository;
        loadData();

    }

    /**
     * load data in memory
     */
    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while((linie=br.readLine())!=null){
                List<String> attr=Arrays.asList(linie.split(";"));
                E e=extractEntity(attr);
                super.save(e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //sau cu lambda - curs 4, sem 4 si 5
//        Path path = Paths.get(fileName);
//        try {
//            List<String> lines = Files.readAllLines(path);
//            lines.forEach(linie -> {
//                E entity=extractEntity(Arrays.asList(linie.split(";")));
//                super.save(entity);
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);
    ///Observatie-Sugestie: in locul metodei template extractEntity, puteti avea un factory pr crearea instantelor entity

    /**
     *create a string having an entity of type E
     * @param entity
     * @return
     *         an entity of type String
     */
    protected abstract String createEntityAsString(E entity);

    @Override
    public E save(E entity){
        E e=super.save(entity);
        if (e==null)
        {
            writeToFile(entity);
        }
        return e;

    }

    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        if(e != null){
            this.reloadData();
        }
        return e;
    }

    /**
     * write data in file
     * @param entity
     *          entity must be not null
     */
    protected void writeToFile(E entity){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * reload data
     */
    public void reloadData() {
        Iterable<E> mapEntities = findAll();

        try {
            PrintWriter printWriter = new PrintWriter(fileName);
            printWriter.print("");
            printWriter.close();
        }catch (FileNotFoundException e){
            System.err.println("file not exist!");
        }
        mapEntities.forEach((e) -> this.writeToFile(e));

    }






}

