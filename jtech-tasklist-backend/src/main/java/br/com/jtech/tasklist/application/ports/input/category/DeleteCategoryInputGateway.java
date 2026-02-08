package br.com.jtech.tasklist.application.ports.input.category;

public interface DeleteCategoryInputGateway {
    void delete(String id, String userId);
}
