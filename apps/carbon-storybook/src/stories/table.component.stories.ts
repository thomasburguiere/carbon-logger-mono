import {TableComponent} from '@carbonlog/shared-ui';
import {Meta, moduleMetadata, Story} from "@storybook/angular";

export default {
    title: 'TableComponent',
    component: TableComponent,
    decorators: [
        moduleMetadata({
            imports: [],
        })
    ],
} as Meta<TableComponent>;

const Template: Story<TableComponent> = (args: TableComponent) => ({
    props: args,
});

export const Primary = Template.bind({});
Primary.args = {
    tableInput: {
        headers: ['CO2 KGs', 'Date'],
        rows: [{
            header: 2.3,
            remainingCells: [new Date()]
        }, {
            header: 12.3,
            remainingCells: [new Date()]
        }]
    },
};
