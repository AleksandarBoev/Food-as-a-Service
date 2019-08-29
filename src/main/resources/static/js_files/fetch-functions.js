const fetchFunctions = (() => {
    return {
        /**
         * Uses fetch API and sends information to controller.
         * @param path {String} URL at which a rest controller is listening
         * @param object {Object} information in the body
         */
        sendJsonToController: (path, object) => {
            return fetch(path, {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(object),
            });
        }
    }
})();