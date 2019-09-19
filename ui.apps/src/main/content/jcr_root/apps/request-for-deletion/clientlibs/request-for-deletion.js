(function($, $document) {
    $(document).on("foundation-contentloaded", function(e) {
        var requestForDeletion = ".cq-damadmin-admin-actions-request-delete-activator";

        $(document).off("click", requestForDeletion).on("click", requestForDeletion, function(e) {
            var paths = [];
            var selectedItems = $(".foundation-selections-item");
            selectedItems.each(function() {
                paths.push($(this).get(0).getAttribute('data-foundation-collection-item-id'));
            });
            $.ajax({
                type: 'GET',
                url: '/bin/flash/deleteassets',
                data: 'assetPaths=' + paths,
                success: function(msg) {
                    alert('Assets are requested for deletion');
                }
            });
        });

    });

})(jQuery, jQuery(document));