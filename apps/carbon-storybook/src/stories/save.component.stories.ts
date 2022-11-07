import {SaveComponent} from "@carbonlog/shared-ui";
import {Meta, moduleMetadata, Story} from "@storybook/angular";


export default {
    title: "SaveComponent",
    component: SaveComponent,
    decorators: [
        moduleMetadata({
            imports: [],
        })
    ],
    argTypes: {
        valueChanged: {action: "value saved"}
    }
} as Meta<SaveComponent>;

const Template: Story<SaveComponent> = (args: SaveComponent) => ({
    props: args,
});

export const Primary = Template.bind({});
Primary.args = {
    initialValue: "1.2",
    placeholder: "2.3"
};
