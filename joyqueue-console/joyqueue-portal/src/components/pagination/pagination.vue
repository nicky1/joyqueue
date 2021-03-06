<template>
  <!-- S 极简分页 -->
  <ul
    v-if="simple"
    :class="simpleClasses">
    <li
      :title="t(localePrefix + 'prevText')"
      :class="prevClasses"
      @click="handlePrev">
      <Icon name="chevron-left"></Icon>
    </li>
    <div :class="[prefixCls + '__simple-paging']">
      <input type="text" :class="[prefixCls + '__original']" :value="currentPage" @keydown="handleKeydown" @keyup="handleKeyup"
             @change="handleKeyup">
      <span>/</span>
      <span :class="[prefixCls + '__paging-total']">{{ totalPage }}</span>
    </div>
    <li
      :title="t(localePrefix + 'nextText')"
      :class="nextClasses"
      @click="handleNext">
      <Icon name="chevron-right"></Icon>
    </li>
  </ul>
  <!-- E 极简分页 -->
  <!-- S 基础分页 -->
  <ul
    v-else
    :class="classes">
    <span :class="[prefixCls + '__total']" v-show="showTotal">
      <slot name="total">{{ `${t(localePrefix + 'total')} ${total} ${t(localePrefix + 'items')}` }}</slot>
    </span>
    <li
      :title="t(localePrefix + 'prevText')"
      :class="prevClasses"
      @click="handlePrev">
      <Icon name="chevron-left"></Icon>
    </li>
    <template v-if="totalPage < 9">
      <li
        v-for="num in pageRange" :key="num"
        :class="[prefixCls + '__item', currentPage === num ? prefixCls + '__item--active' : '']"
        @click="changePage(num)">{{ num }}
      </li>
    </template>
    <template v-else>
      <li :class="[prefixCls + '__item', currentPage === 1 ? prefixCls + '__item--active' : '']"
          @click="changePage(1)">1
      </li>
      <li :class="[prefixCls + '__item', prefixCls + '__item--jump-prev']"
          :title="t(localePrefix + 'prev5Text')"
          v-if="currentPage > 4" @click="handleJumpPrev">
        <Icon name="chevrons-left"></Icon>
      </li>
      <li :class="[prefixCls + '__item']" v-if="currentPage > 3" @click="changePage(currentPage - 2)">{{ currentPage - 2 }}</li>
      <li :class="[prefixCls + '__item']" v-if="currentPage > 2" @click="changePage(currentPage - 1)">{{ currentPage - 1 }}</li>
      <li :class="[prefixCls + '__item', prefixCls + '__item--active']" v-if="currentPage !== 1 && currentPage !== totalPage">{{ currentPage }}</li>
      <li :class="[prefixCls + '__item']" v-if="currentPage < totalPage - 1" @click="changePage(currentPage + 1)">{{ currentPage + 1 }}</li>
      <li :class="[prefixCls + '__item']" v-if="currentPage < totalPage - 2" @click="changePage(currentPage + 2)">{{ currentPage + 2 }}</li>
      <li :class="[prefixCls + '__item', prefixCls + '__item--jump-next']"
          :title="t(localePrefix + 'next5Text')"
          v-if="currentPage < totalPage - 3"
          @click="handleJumpNext">
        <Icon name="chevrons-right"></Icon>
      </li>
      <li :class="[prefixCls + '__item', currentPage === totalPage ? prefixCls + '__item--active' : '']"
          v-if="totalPage > 1"
          @click="changePage(totalPage)">{{ totalPage }}
      </li>
    </template>
    <li
      :title="t(localePrefix + 'nextText')"
      :class="nextClasses"
      @click="handleNext">
      <Icon name="chevron-right"></Icon>
    </li>
    <div v-if="showSizer" :class="[prefixCls + '__sizer']">
      <d-select v-model="currentPageSize" :size="size || 'small'" :transfer="true" @on-change="changeSize">
        <d-option v-for="item in pageSizeOpts" :key="item" :value="item">
          {{ `${item} ${t(localePrefix + 'pageSize')}` }}
        </d-option>
      </d-select>
    </div>
    <div :class="[prefixCls + '__quickjump']" v-if="showQuickjump">
      <span>{{ t(localePrefix + 'goto') }}</span>
      <input type="text" :class="[prefixCls + '__original']" v-model="jumpPageNum" @keydown="handleKeydown"
             @keyup.enter="changePage()">
      <span>{{ t(localePrefix + 'pageText') }}</span>
    </div>
  </ul>
  <!-- E 基础分页 -->
</template>

<script>
import Config from '../../config'
import Icon from '../icon/icon.vue'
import DSelect from '../select/select.vue'
import DOption from '../select/option.vue'
import Locale from '../../mixins/locale'

const prefixCls = `${Config.clsPrefix}pagination`
const localePrefix = `${Config.localePrefix}.pagination.`

export default {
  name: `${Config.namePrefix}Pagination`,
  mixins: [Locale],
  components: {
    Icon,
    DSelect,
    DOption
  },
  props: {
    current: {
      type: Number,
      default: 1
    },
    total: {
      type: Number,
      required: true,
      default: 0
    },
    pageSize: {
      type: Number,
      default: 10
    },
    size: {
      type: String
    },
    simple: {
      type: Boolean,
      default: false
    },
    showTotal: {
      type: Boolean,
      default: false
    },
    showQuickjump: {
      type: Boolean,
      default: false
    },
    showSizer: {
      type: Boolean,
      default: false
    },
    pageSizeOpts: {
      type: Array,
      default () {
        return [10, 20, 30, 40, 50, 100]
      }
    }
  },
  data () {
    return {
      prefixCls: prefixCls,
      localePrefix: localePrefix,
      currentPage: this.current,
      currentPageSize: this.pageSize,
      jumpPageNum: this.current
    }
  },
  watch: {
    current (val) {
      this.currentPage = val
    },
    pageSize (val) {
      this.currentPageSize = val
    }
  },
  computed: {
    simpleClasses () {
      return [
        `${prefixCls}`,
        `${prefixCls}--simple`,
        {
          [`${prefixCls}--${this.size}`]: !!this.size
        }
      ]
    },
    classes () {
      return [
        `${prefixCls}`,
        {
          [`${prefixCls}--${this.size}`]: !!this.size
        }
      ]
    },
    prevClasses () {
      return [
        `${prefixCls}__prev`,
        {
          [`${prefixCls}--disabled`]: this.currentPage === 1
        }
      ]
    },
    nextClasses () {
      return [
        `${prefixCls}__next`,
        {
          [`${prefixCls}--disabled`]: this.currentPage === this.totalPage
        }
      ]
    },
    totalPage () {
      const num = Math.ceil(this.total / this.currentPageSize)
      return (num === 0) ? 1 : num
    },
    visiblePage () {
      return this.totalPage >= 5 ? 5 : this.totalPage
    },
    pageRange () {
      const range = []
      let start = 1

      if (this.currentPage + (this.visiblePage / 2) > this.totalPage) {
        start = this.totalPage - this.visiblePage + 1
        start = start > 0 ? start : 1
      } else if (this.currentPage - (this.visiblePage / 2) > 0) {
        start = Math.ceil(this.currentPage - (this.visiblePage / 2))
      }

      for (let i = 0; i < this.visiblePage && i < this.totalPage; i++) {
        range.push(start + i)
      }

      return range
    }
  },
  methods: {
    changePage (page) {
      let num = (page || this.jumpPageNum || 1) | 0
      num = num > this.totalPage ? this.totalPage : num
      num = num < 1 ? 1 : num

      if (this.currentPage !== num) {
        this.jumpPageNum = num
        this.currentPage = num
        this.$emit('page-change', num)
      }
    },
    handlePrev () {
      const page = this.currentPage
      if (page <= 1) return false
      this.changePage(page - 1)
    },
    handleNext () {
      const page = this.currentPage
      if (page >= this.totalPage) return false
      this.changePage(page + 1)
    },
    handleJumpPrev () {
      const page = this.currentPage - 5
      page ? this.changePage(page) : this.changePage(1)
    },
    handleJumpNext () {
      const page = this.currentPage + 5
      page > this.totalPage ? this.changePage(this.totalPage) : this.changePage(page)
    },
    handleKeydown (evt) {
      const key = evt.keyCode

      // 按键 0~9，空格，左右方向键
      if (!((key >= 48 && key <= 57) || key === 8 || key === 37 || key === 39)) {
        evt.preventDefault()
      }
    },
    handleKeyup (evt) {
      const key = evt.keyCode
      const numVal = evt.target.value | 0

      if (key === 40) { // Up Arrow
        this.handlePrev()
      } else if (key === 38) { // Down Arrow
        this.handleNext()
      } else if (key === 13) { // Return
        let page = 1

        page = (numVal > this.totalPage) ? this.totalPage : numVal
        page = (numVal <= 0) ? 1 : numVal
        evt.target.value = page
        this.changePage(page)
      }
    },
    changeSize (size) {
      this.currentPageSize = size
      this.changePage(1)
      this.$emit('pagesize-change', size)
    }
  }
}
</script>
