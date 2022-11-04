import {Component, Input} from "@angular/core";

export interface TableInput {
    headers: string[];
    rows: TableRow[];
}

export interface TableRow {
    header: any;
    remainingCells: any[];
}

export const emptyTableInput: TableInput = {
    headers: [],
    rows: []
};

@Component({
    selector: 'crb-table',
    template: `
        <div *ngIf="tableInput && tableInput.rows.length > 0" class="overflow-x-auto relative">
            <table class="w-full text-sm text-left text-gray-500 dark:text-gray-400">
                <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                <tr>
                    <th *ngFor="let header of tableInput.headers" scope="col" class="py-3 px-6">
                        {{header}}
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr *ngFor="let row of tableInput.rows"
                    class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
                    <th scope="row" class="py-4 px-6 font-medium text-gray-900 whitespace-nowrap dark:text-white">
                        {{row.header}}
                    </th>
                    <td *ngFor="let cell of row.remainingCells" class="py-4 px-6">
                        {{cell}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>`
})
export class TableComponent {
    @Input() tableInput: TableInput | null = emptyTableInput;
}
