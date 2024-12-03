package org.example.service;

public interface CrossConveyor {
    /**
     * Доступ к конвейеру с определенным идентификатором
     * @param id - идентификатор конвейера
     * @return экземпляр ConveyorAccessor
     */
    ConveyorAccessor conveyor(String id);

    interface ConveyorAccessor {
        /**
         * Помещает новый элемент в структуру
         * @param value - помещаемое значение
         * @return последний элемент целевого конвейера
         */
        int put(int value);
    }
}
